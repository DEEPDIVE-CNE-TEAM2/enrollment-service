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

    @PostMapping
    public ResponseEntity<EnrollmentResponse> enroll(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody EnrollmentRequest request
    ) {
        log.info("수강 신청 요청 - userId={}, programId={}", userId, request.getProgramId());
        EnrollmentResponse response = enrollmentService.enroll(userId, request);
        log.info("수강 신청 완료 - userId={}, programId={}", userId, request.getProgramId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> cancelEnrollmentByUser(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ) {
        log.info("수강 신청 취소 요청 - enrollmentId={}, userId={}", id, userId);

        enrollmentService.cancelEnrollmentByUser(id, userId);

        log.info("수강 신청 취소 완료 - enrollmentId={}, userId={}", id, userId);
        return ResponseEntity.ok(new MessageResponse("수강 신청이 취소되었습니다."));
    }

    @GetMapping("/me")
    public ResponseEntity<List<EnrollmentResponse>> getMyEnrollments(
            @RequestHeader("X-User-Id") Long userId
    ) {
        log.info("내 수강 신청 목록 조회 - userId={}", userId);

        List<EnrollmentResponse> enrollments = enrollmentService.getMyEnrollments(userId);

        log.info("수강 신청 {}건 반환 - userId={}", enrollments.size(), userId);
        return ResponseEntity.ok(enrollments);
    }
}