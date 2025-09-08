package com.moyeorak.enrollment_service.service;

import com.moyeorak.common.exception.BusinessException;
import com.moyeorak.common.exception.ErrorCode;
import com.moyeorak.enrollment_service.client.ProgramClient;
import com.moyeorak.enrollment_service.client.UserClient;
import com.moyeorak.enrollment_service.dto.EnrollmentRequest;
import com.moyeorak.enrollment_service.dto.EnrollmentResponse;
import com.moyeorak.enrollment_service.dto.ProgramDto;
import com.moyeorak.enrollment_service.dto.UserDto;
import com.moyeorak.enrollment_service.entity.Enrollment;
import com.moyeorak.enrollment_service.mapper.EnrollmentMapper;
import com.moyeorak.enrollment_service.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserClient userClient;
    private final ProgramClient programClient;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    @Transactional
    public EnrollmentResponse enroll(Long userId, EnrollmentRequest request) {

        // 사용자, 프로그램 정보 조회
        UserDto user = userClient.getUserById(userId);
        ProgramDto program = programClient.getProgramById(request.getProgramId());
        log.info("받아온 프로그램: id={}, capacity={}, title={}",
                program.getId(), program.getCapacity(), program.getTitle());

                // 정원 초과 체크
        int currentCount = enrollmentRepository.countByProgramIdAndStatus(program.getId(), Enrollment.Status.ENROLLED);
        if (currentCount > program.getCapacity()) {
            log.warn("정원 초과 - programId: {}, 신청자: {}", program.getId(), userId);
            throw new BusinessException(ErrorCode.PROGRAM_FULL);
        }

        // 중복 신청 체크
        List<Enrollment> enrollments = enrollmentRepository.findAllByUserIdAndProgramId(user.getId(), program.getId());
        boolean hasActiveEnrollment = enrollments.stream()
                .anyMatch(e -> e.getStatus() == Enrollment.Status.ENROLLED);
        if (hasActiveEnrollment) {
            log.debug("중복 신청 시도 - userId: {}, programId: {}", userId, request.getProgramId());
            throw new BusinessException(ErrorCode.ALREADY_ENROLLED);
        }

        // 가격 결정
        boolean inRegion = Objects.equals(user.getRegionId(), program.getRegionId());
        int paidAmount = inRegion ? program.getInPrice() : program.getOutPrice();

        // 신규 신청 저장
        Enrollment enrollment = Enrollment.builder()
                .userId(user.getId())
                .programId(program.getId())
                .regionId(program.getRegionId())
                .paidAmount(paidAmount)
                .classStartTime(program.getClassStartTime())
                .classEndTime(program.getClassEndTime())
                .status(Enrollment.Status.ENROLLED)
                .build();

        Enrollment saved = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(saved);
    }

    public void cancelEnrollmentByUser(Long id, Long userId) {

        // 수강 신청 데이터 조회
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ENROLLMENT));

        // 본인 확인
        if (!Objects.equals(enrollment.getUserId(), userId)) {
            log.debug("본인 아님 - 요청자 userId: {}, 소유자 userId: {}", userId, enrollment.getUserId());
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ENROLLMENT_CANCEL);
        }

        // 이미 취소됐는지 확인
        if (enrollment.getStatus() == Enrollment.Status.CANCELLED) {
            log.debug("이미 취소된 신청 - enrollmentId: {}", id);
            throw new BusinessException(ErrorCode.ALREADY_CANCELLED);
        }

        // 취소 가능 기간 체크
        ProgramDto program = programClient.getProgramById(enrollment.getProgramId());
        if (program.getCancelEndDate() != null &&
                LocalDate.now().isAfter(program.getCancelEndDate())) {
            log.warn("취소 기간 만료 - enrollmentId: {}, 취소마감일: {}", id, program.getCancelEndDate());
            throw new BusinessException(ErrorCode.CANCEL_PERIOD_EXPIRED);
        }

        // 상태 변경
        enrollment.cancel("");

        enrollmentRepository.save(enrollment);
    }


    @Override
    public List<EnrollmentResponse> getMyEnrollments(Long userId) {
        return enrollmentRepository.findByUserId(userId).stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

}