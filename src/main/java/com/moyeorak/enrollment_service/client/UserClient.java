package com.moyeorak.enrollment_service.client;

import com.moyeorak.enrollment_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth", url = "http://localhost:8080")
public interface UserClient {

    @GetMapping("/api/users/email")
    UserDto getUserByEmail(@RequestParam("email") String email);
    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);
}