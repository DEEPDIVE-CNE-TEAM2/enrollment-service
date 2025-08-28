package com.moyeorak.enrollment_service.client;

import com.moyeorak.enrollment_service.client.ProgramClient;
import com.moyeorak.enrollment_service.dto.ProgramDto;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class ProgramClientMock implements ProgramClient {

    @Override
    public ProgramDto getProgramById(Long programId) {
        return ProgramDto.builder()
                .id(programId)
                .regionId(1L)
                .inPrice(10000)
                .outPrice(20000)
                .classStartTime(LocalTime.of(9, 0))
                .classEndTime(LocalTime.of(11, 0))
                .instructorName("홍길동")
                .cancelEndDate(null)
                .build();
    }
}