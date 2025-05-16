package com.example.emotion_analysis.dto;

import com.example.emotion_analysis.entity.EmotionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
@Schema(description = "감정 분석 결과 (감정별 백분율 분포)")
public class EmotionResultDto {

    @Schema(
      description = "joy, sadness, anger, calm, anxiety 각 감정에 대한 백분율 분포 맵",
      example = "{\"joy\":40.5, \"sadness\":10.0, \"anger\":5.0, \"calm\":30.0, \"anxiety\":14.5}"
    )
    private Map<EmotionType, Double> distribution;

    /**
     * Map에서 확률이 가장 높은 감정 하나를 반환합니다.
     */
    public EmotionType getTopEmotion() {
        return distribution.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(EmotionType.calm);
    }
}