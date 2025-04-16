package com.example.emotion_analysis.service;

import org.springframework.stereotype.Service;

import com.example.emotion_analysis.dto.EmotionResultDto;
import com.example.emotion_analysis.entity.*;
import com.example.emotion_analysis.repository.EmotionLogRepository;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class EmotionService {

    private final ChatGptClient chatGptClient;
    private final EmotionLogRepository emotionLogRepository;

    public EmotionResultDto analyzeEmotion(String text, Long userId) {
        String prompt = "다음 문장의 감정을 분석해줘. 감정은 joy, sadness, anger, calm, anxiety 중 하나로 답해줘.\n문장: " + text;

        String emotionText = chatGptClient.getEmotionFromText(prompt);
        EmotionType emotion = EmotionType.valueOf(emotionText); // enum으로 변환

        // 💾 DB 저장
        EmotionLog log = EmotionLog.builder()
                .userId(userId)
                .text(text)
                .emotion(emotion)
                .createdAt(LocalDateTime.now())
                .build();

        emotionLogRepository.save(log);

        return new EmotionResultDto(emotionText);
    }
}
