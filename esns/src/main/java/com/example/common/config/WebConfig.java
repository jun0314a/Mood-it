package com.example.common.config;

import org.springframework.lang.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${frontend.origin:http://localhost:19006}")
    private String frontendOrigin;

    // application.properties에 정의한 프로필 이미지 저장 경로
    @Value("${file.upload-dir.profile}")
    private String profileUploadDir;

    // application.properties에 정의한 스토리 이미지 저장 경로
    @Value("${file.upload-dir.story}")
    private String storyUploadDir;

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
        // 1) 프로필 이미지 (/profile-images/**) 요청을 profileUploadDir로 매핑
        registry.addResourceHandler("/profile-images/**")
                .addResourceLocations("file:" + profileUploadDir);

        // 2) 스토리 이미지 (/story-images/**) 요청을 storyUploadDir로 매핑
        registry.addResourceHandler("/story-images/**")
                .addResourceLocations("file:" + storyUploadDir);
    }
}
