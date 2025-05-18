package com.example.emotion_recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@Schema(description = "감정 기반 추천 응답 정보")
public class RecommendationResponseDto {

    @Schema(description = "요청한 감정", example = "joy")
    private String emotion;

    @Schema(description = "추천 콘텐츠 리스트")
    private List<ContentRecommendationDto> recommendations;
}
