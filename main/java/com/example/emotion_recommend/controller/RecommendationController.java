package com.example.emotion_recommend.controller;

import com.example.emotion_recommend.dto.RecommendationRequestDto;
import com.example.emotion_recommend.dto.RecommendationResponseDto;
import com.example.emotion_recommend.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<RecommendationResponseDto> getRecommendations(@RequestBody RecommendationRequestDto requestDto) {
        RecommendationResponseDto response = recommendationService.getRecommendationsByEmotion(requestDto.getEmotion());
        return ResponseEntity.ok(response);
    }
}
