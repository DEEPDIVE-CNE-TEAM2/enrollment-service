package com.moyeorak.enrollment_service.service;

import com.moyeorak.enrollment_service.dto.EnrollmentRequest;
import com.moyeorak.enrollment_service.dto.EnrollmentResponse;
import com.moyeorak.enrollment_service.entity.Enrollment;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse enrollByEmail(String email, EnrollmentRequest request);
    void cancelEnrollmentByUser(Long id, Long userId);
}
