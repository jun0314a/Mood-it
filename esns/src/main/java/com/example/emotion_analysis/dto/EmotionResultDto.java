package com.example.emotion_analysis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "감정 분석 결과")
public class EmotionResultDto {

    @Schema(description = "분석된 감정", example = "joy")
    private String emotion;
}
