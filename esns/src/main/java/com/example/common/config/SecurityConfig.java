package com.example.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.auth.jwt.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (API 사용 시 필요)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션X
            .authorizeHttpRequests(auth -> auth
                // 공용 엔드포인트
                .requestMatchers(
                    "/api/auth/login",
                    "/api/users/signup",
                    "/api/users/verify-email",
                    "/api/users/send-code",
                    "/api/users/forgot-password",
                    "/api/users/reset-password",
                    "/error",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/api-docs/**"
                ).permitAll()
                 // 업로드 파일은 인증 없이 허용
                .requestMatchers("/uploads/**").permitAll()
                // 스토리 이미지 URL 허용용
                .requestMatchers("/story/**").permitAll()
                // 프로필 이미지 URL 허용
                .requestMatchers("/profile/**").permitAll()
                // 그룹룹 이미지 URL 허용
                .requestMatchers("/group/**").permitAll()
                // 프로필 조회는 퍼블릭으로 허용
                .requestMatchers(HttpMethod.GET, "/api/users/*").permitAll()
                // 그 외 요청은 인증 필요
                .anyRequest().authenticated()
            )
            // 예외 처리 설정
            .exceptionHandling(ex -> ex
                // 인증 실패 시 401
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                // 권한 부족 시 403
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
            )
            // JWT 필터 등록
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // AuthenticationManager 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 비밀번호 암호화 방식 (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
