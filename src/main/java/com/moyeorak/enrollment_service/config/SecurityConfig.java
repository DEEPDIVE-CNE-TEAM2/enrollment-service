package com.moyeorak.enrollment_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 개발 중엔 CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        // Swagger 허용
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**",
                                "/webjars/**"
                        ).permitAll()
                        //.anyRequest().authenticated()
                        // 임시도 전체 허용 나중에 삭제
                        .anyRequest().permitAll()

                )
                // 로그인 화면 없이 401 Unauthorized 반환
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable());

        return http.build();
    }
}
