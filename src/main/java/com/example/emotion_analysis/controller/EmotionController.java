package com.example.emotion_analysis.controller;

import com.example.emotion_analysis.dto.EmotionRequestDto;
import com.example.emotion_analysis.dto.EmotionResultDto;
import com.example.emotion_analysis.dto.EmotionStatsDto;
import com.example.emotion_analysis.service.EmotionService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(
      summary = "감정 분석",
      description = "JWT 토큰으로 식별된 사용자에 대해 전달된 텍스트의 감정을 분석하여 결과를 반환합니다."
    )
    @ApiResponses({
      @ApiResponse(
        responseCode = "200",
        description = "분석 성공",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = EmotionResultDto.class),
          examples = @ExampleObject(value = "{\"emotion\":\"joy\"}")
        )
      ),
      @ApiResponse(responseCode = "400", description = "잘못된 요청"),
      @ApiResponse(responseCode = "401", description = "토큰 없음 또는 유효하지 않음")
    })
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
        @Operation(
      summary = "이 달의 감정 통계",
      description = "토큰 또는 경로변수로 주어진 사용자에 대해 이번 달 joy, sadness, anger, calm, anxiety 비율을 반환합니다."
    )
    @ApiResponses({
      @ApiResponse(
        responseCode = "200",
        description = "통계 조회 성공",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = EmotionStatsDto.class),
          examples = @ExampleObject(value = 
            "{\"joy\":0.40,\"sadness\":0.15,\"anger\":0.10,\"calm\":0.25,\"anxiety\":0.10}"
          )
        )
      ),
      @ApiResponse(responseCode = "401", description = "토큰 없음 또는 유효하지 않음")
    })
    @GetMapping("/stats/{userId}")
    public ResponseEntity<EmotionStatsDto> getMonthlyStats(
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        // (선택) 토큰 검증만 하고 userId는 경로에서 가져오기
        String token = extractTokenFromHeader(request);
        jwtUtil.validateToken(token);

        EmotionStatsDto stats = emotionService.getMonthlyStats(userId);
        return ResponseEntity.ok(stats);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("JWT 토큰이 없습니다.");
    }
}
