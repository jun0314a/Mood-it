package com.example.emotion_analysis.service;

import com.example.emotion_analysis.dto.EmotionResultDto;
import com.example.emotion_analysis.entity.EmotionLog;
import com.example.emotion_analysis.entity.EmotionType;
import com.example.emotion_analysis.repository.EmotionLogRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmotionService {

    private final ChatGptClient chatGptClient;
    private final EmotionLogRepository emotionLogRepository;
    private final ObjectMapper objectMapper;

    public EmotionResultDto analyzeEmotion(String text, Long userId) {
        // 1) 분포 요청 프롬프트 구성
        String prompt = """
            다음 문장의 감정을 분석하고,
            joy, sadness, anger, calm, anxiety 다섯 가지에 대해
            각각 백분율(%%) 형태로 JSON으로 답해줘.
            예시: {"joy":40.5, "sadness":10.0, "anger":5.0, "calm":30.0, "anxiety":14.5}
            문장: %s
            """.formatted(text);

        // 2) ChatGPT 호출 → JSON 문자열 반환
        String json = chatGptClient.getEmotionDistribution(prompt);

        // 3) JSON → Map<EmotionType, Double> 파싱
        Map<EmotionType, Double> distribution;
        try {
            distribution = objectMapper.readValue(
                json,
                new TypeReference<Map<EmotionType, Double>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("감정 분포 파싱에 실패했습니다: " + json, e);
        }

        // 4) 가장 확률이 높은 감정 하나 선택
        EmotionType topEmotion = distribution.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(EmotionType.calm);

        // 5) DB 저장 (최다 감정 하나만)
        EmotionLog log = EmotionLog.builder()
            .userId(userId)
            .text(text)
            .emotion(topEmotion)
            .createdAt(LocalDateTime.now())
            .build();
        emotionLogRepository.save(log);

        // 6) 전체 분포 DTO 반환
        return new EmotionResultDto(distribution);
    }

        public EmotionResultDto analyzeAndSave(String text, Long userId) {
        return analyzeEmotion(text, userId);
    }
}
