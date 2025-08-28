package com.moyeorak.enrollment_service.service;

import com.moyeorak.common.exception.BusinessException;
import com.moyeorak.common.exception.ErrorCode;
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

import java.time.LocalTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional
    public EnrollmentResponse enrollByEmail(String email, EnrollmentRequest request) {
        log.info("[ENROLL] 수강 신청 요청 - email: {}, programId: {}", email, request.getProgramId());

        // 사용자 & 프로그램 정보 조회 (지금은 Mock)
        UserDto user = getUserMock(email);
        ProgramDto program = getProgramMock(request.getProgramId());

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

    // 임시 데이터 나중에 삭제
    private UserDto getUserMock(String email) {
        return UserDto.builder()
                .id(2L)                  // 임시 유저 ID
                .email(email)            // 요청에서 받은 이메일 그대로 사용
                .regionId(2L)            // 임시로 1번 지역 고정
                .build();
    }
    private ProgramDto getProgramMock(Long programId) {
        return ProgramDto.builder()
                .id(programId)                  // 요청에서 받은 프로그램 ID 그대로 사용
                .regionId(1L)                   // 임시로 프로그램 지역도 1번으로 고정
                .inPrice(10000)                 // 관내 가격 (Mock)
                .outPrice(20000)                // 관외 가격 (Mock)
                .classStartTime(LocalTime.of(9, 0))   // 임시 시작 시간
                .classEndTime(LocalTime.of(11, 0))    // 임시 종료 시간
                .build();
    }
}