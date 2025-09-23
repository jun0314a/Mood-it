package com.example.common.config;

import org.springframework.lang.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File; // File 클래스 임포트
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${frontend.origin:http://localhost:19006}")
    private String frontendOrigin;

    // application.properties에 정의된 기본 업로드 경로 (예: 'uploads')를 주입받습니다.
    @Value("${upload.path}") // <-- application.properties의 upload.path=uploads 값을 사용
    private String baseUploadPath;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // 🚨 기존의 모든 registry.addResourceHandler(...) 설정들을 제거하고 이 단일 설정으로 대체합니다. 🚨
        // '/uploads/**' URL 패턴으로 들어오는 모든 요청을
        // 실제 파일 시스템의 'uploads' 디렉토리 (baseUploadPath)로 매핑합니다.
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + File.separator + baseUploadPath + File.separator);
    }

    @Override
    public void configureMessageConverters(@NonNull List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        converter.setObjectMapper(new ObjectMapper());
        converters.add(converter);
    }
}