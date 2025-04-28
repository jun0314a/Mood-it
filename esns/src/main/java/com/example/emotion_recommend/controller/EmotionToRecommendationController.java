package com.example.emotion_recommend.controller;

import com.example.emotion_analysis.dto.EmotionRequestDto;
import com.example.emotion_analysis.service.EmotionService;
import com.example.auth.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class EmotionToRecommendationController {

    private final EmotionService emotionService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<String> recommendByEmotion(
            @RequestBody EmotionRequestDto requestDto,
            HttpServletRequest request
    ) {
        // 🔐 JWT에서 userId 추출
        String token = extractTokenFromHeader(request);
        Long userId = jwtUtil.extractUserId(token);

        // ✅ userId와 함께 감정 분석 요청
        String emotion = emotionService.analyzeEmotion(requestDto.getText(), userId).getEmotion();

        // 👉 이후 emotion을 기반으로 추천 로직 이어가면 됨 (예: 추천 서비스 호출)
        return ResponseEntity.ok("분석된 감정: " + emotion);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("JWT 토큰이 없습니다.");
    }
}
