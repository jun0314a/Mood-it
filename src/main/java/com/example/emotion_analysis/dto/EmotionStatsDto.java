package com.example.emotion_analysis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmotionStatsDto {
    @Schema(description = "joy 비율", example = "0.40")
    private double joy;

    @Schema(description = "sadness 비율", example = "0.15")
    private double sadness;

    @Schema(description = "anger 비율", example = "0.10")
    private double anger;

    @Schema(description = "calm 비율", example = "0.25")
    private double calm;

    @Schema(description = "anxiety 비율", example = "0.10")
    private double anxiety;
}
