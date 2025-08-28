package com.moyeorak.enrollment_service.client;

import com.moyeorak.enrollment_service.dto.UserDto;

public interface UserClient {

    UserDto getUserById(Long userId);

}