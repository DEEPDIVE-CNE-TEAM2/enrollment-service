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

    @Override
    @Transactional
    public EnrollmentResponse enrollByEmail(String email, EnrollmentRequest request) {
        log.info("[ENROLL] 수강 신청 요청 - email: {}, programId: {}", email, request.getProgramId());

        // 사용자, 프로그램 정보 조회 (지금은 Mock)
        UserDto user = userClient.getUserByEmail(email);
        ProgramDto program = programClient.getProgramById(request.getProgramId());

        // 가격 결정
        boolean inRegion = Objects.equals(user.getRegionId(), program.getRegionId());
        int paidAmount = inRegion ? program.getInPrice() : program.getOutPrice();

        // 중복 신청 체크
        enrollmentRepository.findByUserIdAndProgramId(user.getId(), program.getId())
                .ifPresent(e -> {
                    if (e.getStatus() != Enrollment.Status.CANCELLED) {
                        throw new BusinessException(ErrorCode.ALREADY_ENROLLED);
                    }
                });

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

        // 응답 생성
        return EnrollmentResponse.builder()
                .id(saved.getId())
                .userId(saved.getUserId())
                .programId(saved.getProgramId())
                .regionId(saved.getRegionId())
                .paidAmount(saved.getPaidAmount())
                .status(saved.getStatus().name())
                .build();
    }

    public void cancelEnrollmentByUser(Long id, Long userId) {
        log.info("[CANCEL] 사용자 수강 취소 요청 - enrollmentId: {}, userId: {}", id, userId);

        // 수강 신청 데이터 조회
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ENROLLMENT));

        // 본인 확인
        if (!Objects.equals(enrollment.getUserId(), userId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ENROLLMENT_CANCEL);
        }

        // 이미 취소됐는지 확인
        if (enrollment.getStatus() == Enrollment.Status.CANCELLED) {
            throw new BusinessException(ErrorCode.ALREADY_CANCELLED);
        }

        // 취소 가능 기간 체크
        ProgramDto program = programClient.getProgramById(enrollment.getProgramId());
        if (program.getCancelEndDate() != null &&
                LocalDate.now().isAfter(program.getCancelEndDate())) {
            throw new BusinessException(ErrorCode.CANCEL_PERIOD_EXPIRED);
        }

        // 상태 변경
        enrollment.cancel("");

        enrollmentRepository.save(enrollment);
    }

}