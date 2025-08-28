package com.moyeorak.enrollment_service.dto;
// 임시
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private Long regionId;
}
