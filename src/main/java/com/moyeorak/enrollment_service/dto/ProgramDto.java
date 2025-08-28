package com.moyeorak.enrollment_service.dto;
// 임시
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramDto {
    private Long id;
    private Long regionId;
    private Integer inPrice;
    private Integer outPrice;
    private LocalTime classStartTime;
    private LocalTime classEndTime;
    private String instructorName;
    private LocalDate cancelEndDate;
}