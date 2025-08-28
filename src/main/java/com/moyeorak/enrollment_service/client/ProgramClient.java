package com.moyeorak.enrollment_service.client;

import com.moyeorak.enrollment_service.dto.ProgramDto;

public interface ProgramClient {

    // 프로그램 ID로 프로그램 조회
    ProgramDto getProgramById(Long programId);
}