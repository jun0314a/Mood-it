package com.example.emotion_recommend.controller;

import com.example.emotion_analysis.dto.EmotionRequestDto;
import com.example.emotion_recommend.dto.RecommendationResponseDto;
import com.example.emotion_recommend.service.RecommendationService;
import com.example.auth.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class EmotionToRecommendationController {

    private final RecommendationService recommendationService;
    private final JwtUtil jwtUtil;

    /**
     * text 한 줄로 감정 분석→저장→콘텐츠 추천까지 처리합니다.
     */
    @PostMapping
    public ResponseEntity<RecommendationResponseDto> recommend(
            @RequestBody EmotionRequestDto requestDto,
            HttpServletRequest request) {

        // JWT에서 userId 추출 (로그 저장용)
        String token = extractToken(request);
        Long userId = jwtUtil.extractUserId(token);

        // 감정 분석 및 추천
        RecommendationResponseDto dto =
            recommendationService.getRecommendationsByText(requestDto.getText(), userId);

        return ResponseEntity.ok(dto);
    }

    private String extractToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new RuntimeException("JWT 토큰이 없습니다.");
    }
}
