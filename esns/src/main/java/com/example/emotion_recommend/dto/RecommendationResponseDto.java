package com.example.emotion_recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendationResponseDto {
    private String emotion;
    private List<ContentRecommendationDto> recommendations;
}
