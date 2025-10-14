package com.springboot.springproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())        // CSRF 비활성화 (API 테스트용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().permitAll()    // 모든 요청 허용
                )
                .formLogin(form -> form.disable())   // 기본 로그인 페이지 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()); // 기본 인증 비활성화

        return http.build();
    }
}
