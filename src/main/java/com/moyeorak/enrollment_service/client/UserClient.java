package com.moyeorak.enrollment_service.client;

import com.moyeorak.enrollment_service.dto.UserDto;

public interface UserClient {

    // 이메일 기반 사용자 조회
    UserDto getUserByEmail(String email);

}