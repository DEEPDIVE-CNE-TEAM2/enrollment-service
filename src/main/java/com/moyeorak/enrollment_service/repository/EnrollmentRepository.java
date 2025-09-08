package com.moyeorak.enrollment_service.repository;

import com.moyeorak.enrollment_service.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByUserId(Long userId);
    List<Enrollment> findAllByUserIdAndProgramId(Long userId, Long programId);

    int countByProgramIdAndStatus(Long programId, Enrollment.Status status);
}