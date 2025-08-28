package com.moyeorak.enrollment_service.mapper;

import com.moyeorak.enrollment_service.client.ProgramClient;
import com.moyeorak.enrollment_service.client.UserClient;
import com.moyeorak.enrollment_service.dto.EnrollmentResponse;
import com.moyeorak.enrollment_service.dto.ProgramDto;
import com.moyeorak.enrollment_service.dto.UserDto;
import com.moyeorak.enrollment_service.entity.Enrollment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EnrollmentMapper {

    private final ProgramClient programClient;
    private final UserClient userClient;

    public EnrollmentResponse toResponse(Enrollment e) {
        // 정보 가져오기
        ProgramDto program = programClient.getProgramById(e.getProgramId());
        UserDto user = userClient.getUserById(e.getUserId());

        // 관내/관외 가격 계산
        boolean inRegion = Objects.equals(user.getRegionId(), program.getRegionId());
        int appliedPrice = inRegion ? program.getInPrice() : program.getOutPrice();
        String regionLabel = inRegion ? "관내" : "관외";

        // EnrollmentResponse 생성
        return EnrollmentResponse.builder()
                .id(e.getId())
                .userId(user.getId())
                .programId(program.getId())
                .regionId(program.getRegionId())
                .enrolledAt(e.getEnrolledAt())
                .status(e.getStatus().name().toLowerCase())
                .paidAmount(appliedPrice)
                .cancelReason(e.getCancelReason())
                .classStartTime(program.getClassStartTime())
                .classEndTime(program.getClassEndTime())
                .instructorName(program.getInstructorName())
                .regionLabel(regionLabel)
                .cancelEndDate(program.getCancelEndDate())
                .build();
    }
}