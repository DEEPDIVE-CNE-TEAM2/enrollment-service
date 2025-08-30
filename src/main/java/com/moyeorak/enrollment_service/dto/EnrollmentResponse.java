package com.moyeorak.enrollment_service.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponse {
    private Long id;
    private Long userId;
    private Long programId;
    private Long regionId;
    private String userEmail;
    private LocalDateTime enrolledAt;
    private String status;
    private Integer paidAmount;
    private String cancelReason;
    private LocalTime classStartTime;
    private LocalTime classEndTime;
    private String instructorName;
    private String regionLabel;
    private LocalDate cancelEndDate;
}