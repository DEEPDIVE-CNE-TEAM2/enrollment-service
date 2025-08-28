package com.moyeorak.enrollment_service.client;

import com.moyeorak.enrollment_service.client.UserClient;
import com.moyeorak.enrollment_service.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserClientMock implements UserClient {

    @Override
    public UserDto getUserByEmail(String email) {
        return UserDto.builder()
                .id(3L)
                .email(email)
                .regionId(2L)
                .build();
    }

}