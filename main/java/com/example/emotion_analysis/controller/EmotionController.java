package com.example.emotion_analysis.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.emotion_analysis.dto.EmotionRequestDto;
import com.example.emotion_analysis.dto.EmotionResultDto;
import com.example.emotion_analysis.service.EmotionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/emotion")
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;

    @PostMapping("/analyze")
    public ResponseEntity<EmotionResultDto> analyzeEmotion(@RequestBody EmotionRequestDto requestDto) {
        EmotionResultDto result = emotionService.analyzeEmotion(requestDto.getText());
        return ResponseEntity.ok(result);
    }
}
