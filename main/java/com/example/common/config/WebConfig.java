package com.example.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins("*") // 모든 도메인 허용 (개발용)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(false); // 인증정보(쿠키) 전달 허용 여부
    }
}