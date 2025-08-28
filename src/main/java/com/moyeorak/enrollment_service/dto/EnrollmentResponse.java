package com.moyeorak.enrollment_service.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponse {
    private Long id;
    private Long userId;
    private Long programId;
    private Long regionId;
    private Integer paidAmount;
    private String status;
}