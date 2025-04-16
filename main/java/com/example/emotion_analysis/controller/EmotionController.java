package com.example.emotion_analysis.controller;

import com.example.emotion_analysis.dto.EmotionRequestDto;
import com.example.emotion_analysis.dto.EmotionResultDto;
import com.example.emotion_analysis.service.EmotionService;
import com.example.auth.jwt.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emotion")
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;
    private final JwtUtil jwtUtil;

    @PostMapping("/analyze")
    public ResponseEntity<EmotionResultDto> analyzeEmotion(
            @RequestBody EmotionRequestDto requestDto,
            HttpServletRequest request
    ) {
        String token = extractTokenFromHeader(request);
        Long userId = jwtUtil.extractUserId(token); // 🔐 토큰에서 userId 추출

        EmotionResultDto result = emotionService.analyzeEmotion(requestDto.getText(), userId);
        return ResponseEntity.ok(result);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("JWT 토큰이 없습니다.");
    }
}
