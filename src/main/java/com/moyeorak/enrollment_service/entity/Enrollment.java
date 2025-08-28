package com.moyeorak.enrollment_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "program_id", nullable = false)
    private Long programId;

    @Column(name = "region_id", nullable = false)
    private Long regionId;

    @CreationTimestamp
    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private LocalDateTime enrolledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String cancelReason;

    @Column(name = "paid_amount")
    private Integer paidAmount;

    @Column(name = "class_start_time", nullable = false)
    private LocalTime classStartTime;

    @Column(name = "class_end_time", nullable = false)
    private LocalTime classEndTime;

    public enum Status {
        ENROLLED, CANCELLED
    }

    // 상태 변경은 명시적 메서드로만 허용
    public void cancel(String reason) {
        this.status = Status.CANCELLED;
        this.cancelReason = reason;
    }
}