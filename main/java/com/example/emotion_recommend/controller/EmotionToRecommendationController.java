package com.example.emotion_recommend.controller;

import com.example.emotion_analysis.dto.EmotionRequestDto;
import com.example.emotion_analysis.service.EmotionService;
import com.example.emotion_recommend.dto.RecommendationResponseDto;
import com.example.emotion_recommend.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations/from-text")
@RequiredArgsConstructor
public class EmotionToRecommendationController {

    private final EmotionService emotionService;
    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<RecommendationResponseDto> analyzeAndRecommend(@RequestBody EmotionRequestDto requestDto) {
        // 1. 감정 분석
        String emotion = emotionService.analyzeEmotion(requestDto.getText()).getEmotion();

        // 2. 해당 감정으로 추천 수행
        RecommendationResponseDto recommendation = recommendationService.getRecommendationsByEmotion(emotion);

        return ResponseEntity.ok(recommendation);
    }
}
