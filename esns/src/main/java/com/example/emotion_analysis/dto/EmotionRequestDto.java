package com.example.emotion_analysis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "감정 분석 요청 정보")
public class EmotionRequestDto {

    @Schema(description = "분석할 텍스트", example = "기분 좋은 하루입니다!", required = true)
    private String text;
}
