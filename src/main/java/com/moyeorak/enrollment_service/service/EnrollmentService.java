package com.moyeorak.enrollment_service.service;

import com.moyeorak.enrollment_service.dto.EnrollmentRequest;
import com.moyeorak.enrollment_service.dto.EnrollmentResponse;

public interface EnrollmentService {
    EnrollmentResponse enrollByEmail(String email, EnrollmentRequest request);
}
