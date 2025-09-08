package com.moyeorak.enrollment_service.client;

import com.moyeorak.enrollment_service.dto.ProgramDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "program", url = "http://localhost:8083")
public interface ProgramClient {

    @GetMapping("/internal/programs/{id}")
    ProgramDto getProgramById(@PathVariable("id") Long id);
}