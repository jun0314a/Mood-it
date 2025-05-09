package com.example.emotion_recommend.controller;

import com.example.emotion_analysis.dto.EmotionRequestDto;
import com.example.emotion_analysis.service.EmotionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(
      summary = "감정 기반 추천 예비 호출",
      description = "JWT 토큰으로 식별된 사용자에 대해 텍스트 감정을 분석 후 추천 로직을 시작합니다. (여기서는 감정 문자열만 반환)"
    )
    @ApiResponses({
      @ApiResponse(
        responseCode = "200",
        description = "감정 분석 완료",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(type = "string"),
          examples = @ExampleObject(value = "\"분석된 감정: joy\"")
        )
      ),
      @ApiResponse(responseCode = "400", description = "잘못된 요청"),
      @ApiResponse(responseCode = "401", description = "토큰 없음 또는 유효하지 않음")
    })
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
