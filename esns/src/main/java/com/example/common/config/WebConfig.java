package com.example.common.config;

import org.springframework.lang.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${frontend.origin:http://localhost:19006}")
    private String frontendOrigin;

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontendOrigin) // 예: http://localhost:3000 or 실제 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // 인증정보(Cookie, Authorization 헤더 등) 허용
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // 스토리 이미지 업로드 파일 접근 허용
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
