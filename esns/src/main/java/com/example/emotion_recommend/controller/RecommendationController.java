package com.example.emotion_recommend.controller;

import com.example.emotion_recommend.dto.RecommendationRequestDto;
import com.example.emotion_recommend.dto.RecommendationResponseDto;
import com.example.emotion_recommend.service.RecommendationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(
      summary = "감정 기반 콘텐츠 추천",
      description = "감정 문자열(joy, sadness 등)을 받아 해당 감정에 맞는 콘텐츠 추천 목록을 반환합니다."
    )
    @ApiResponses({
      @ApiResponse(
        responseCode = "200",
        description = "추천 목록 반환",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RecommendationResponseDto.class)
        )
      ),
      @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<RecommendationResponseDto> getRecommendations(@RequestBody RecommendationRequestDto requestDto) {
        RecommendationResponseDto response = recommendationService.getRecommendationsByEmotion(requestDto.getEmotion());
        return ResponseEntity.ok(response);
    }
}
