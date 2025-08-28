package com.moyeorak.enrollment_service.controller;

import com.moyeorak.enrollment_service.dto.EnrollmentRequest;
import com.moyeorak.enrollment_service.dto.EnrollmentResponse;
import com.moyeorak.enrollment_service.dto.MessageResponse;
import com.moyeorak.enrollment_service.service.EnrollmentService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // api gateway 완성되면 수정 필요
    @PostMapping
    public ResponseEntity<EnrollmentResponse> enroll(
            @RequestHeader("X-User-Email") String email,
            @Valid @RequestBody EnrollmentRequest request
    ) {
        return ResponseEntity.ok(
                enrollmentService.enrollByEmail(email, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> cancelEnrollmentByUser(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ) {
        enrollmentService.cancelEnrollmentByUser(id, userId);
        return ResponseEntity.ok(new MessageResponse("수강 신청이 취소되었습니다."));
    }
}
