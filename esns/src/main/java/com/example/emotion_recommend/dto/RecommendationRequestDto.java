package com.example.emotion_recommend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "감정 기반 추천 요청 정보")
public class RecommendationRequestDto {

    @Schema(description = "감정 키워드 (joy, sadness, anger, calm, anxiety)", example = "joy", required = true)
    private String emotion; // joy, sadness, anger, calm, anxiety
}
