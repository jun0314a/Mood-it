package com.example.emotion_analysis.service;

import org.springframework.stereotype.Service;

import com.example.emotion_analysis.dto.EmotionResultDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmotionService {

    private final ChatGptClient chatGptClient;

    public EmotionResultDto analyzeEmotion(String text) {
        String prompt = "다음 문장의 감정을 분석해줘. 감정은 joy, sadness, anger, calm, anxiety 중 하나로 답해줘.\n문장: " + text;

        String emotion = chatGptClient.getEmotionFromText(prompt);

        return new EmotionResultDto(emotion);
    }
}
