package com.example.common.config;

import org.springframework.beans.factory.annotation.Value; // Value 어노테이션 import 추가
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${frontend.origin:http://localhost:19006}")
    private String frontendOrigin;

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        List<String> allowedOriginPatterns = new ArrayList<>();
        // 1) application.properties의 frontend.origin (콤마 구분) 처리
        if (!frontendOrigin.isEmpty()) {
            String[] origins = frontendOrigin.split(",");
            for (String origin : origins) {
                String trimmed = origin.trim();
                if (!trimmed.isEmpty()) {
                    allowedOriginPatterns.add(trimmed);
                }
            }
        }

        // 2) 로컬 개발 기본값 보강
        allowedOriginPatterns.add("http://localhost:19006");
        allowedOriginPatterns.add("http://localhost:" + serverPort);
        // 3) Expo/터널 도메인 와일드카드 허용 (https)
        allowedOriginPatterns.add("https://*.expo.dev");
        allowedOriginPatterns.add("https://*.ngrok-free.app");

        config.setAllowedOriginPatterns(allowedOriginPatterns);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
