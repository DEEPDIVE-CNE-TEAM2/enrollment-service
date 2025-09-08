package com.moyeorak.enrollment_service.dto;

public record ProgramEnrollmentCountResponse(
        Long programId,
        int enrolledCount
) {}
