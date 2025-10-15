package com.springboot.springproject.config;

import com.springboot.springproject.auth.handler.OAuth2LoginSuccessHandler;
import com.springboot.springproject.auth.jwt.JwtAuthenticationFilter;
import com.springboot.springproject.auth.service.CustomOAuth2UserService;
import com.springboot.springproject.auth.service.CustomOidcUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   OAuth2LoginSuccessHandler successHandler, CustomOAuth2UserService customOAuth2UserService, CustomOidcUserService customOidcUserService) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")  // REST API 요청만 CSRF 무시
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/auth/login", "/auth/check").permitAll()
                        .requestMatchers("/crud", "/kafka", "/ci").permitAll()
                        .requestMatchers("/error", "/error/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/board/**").permitAll()
                        .requestMatchers("/api/board").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 여기서 연결
                                .oidcUserService(customOidcUserService)
                        )
                        .successHandler(successHandler) // JWT 발급 후 리디렉션

                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/") // 로그아웃 후 리디렉션
                        .deleteCookies("JSESSIONID", "JWT_TOKEN")
                );;
        return http.build();
    }

}
