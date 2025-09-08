package com.moyeorak.enrollment_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@ToString
public class ProgramDto {
    private Long id;
    private Long regionId;
    private String title;
    private int inPrice;
    private int outPrice;
    private LocalTime classStartTime;
    private LocalTime classEndTime;
    private String instructorName;
    private LocalDate cancelEndDate;
    private int capacity;
}