package com.moyeorak.enrollment_service.client;

import com.moyeorak.enrollment_service.client.UserClient;
import com.moyeorak.enrollment_service.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserClientMock implements UserClient {

    @Override
    public UserDto getUserById(Long userId) {
        return UserDto.builder()
                .id(userId)
                .email("test@example.com")
                .regionId(2L)
                .build();
    }

}