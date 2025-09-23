package com.example.emotion_analysis.service;

import com.example.emotion_analysis.dto.EmotionResultDto;
import com.example.emotion_analysis.dto.EmotionStatsDto;
import com.example.emotion_analysis.entity.EmotionLog;
import com.example.emotion_analysis.entity.EmotionType;
import com.example.emotion_analysis.repository.EmotionLogRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

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

        /**
     * 이번 달(userId)의 감정 로그를 모두 조회해,
     * joy, sadness, anger, calm, anxiety 비율을 계산해서 반환
     */
    public EmotionStatsDto getMonthlyStats(Long userId) {
        // 1) 이번 달 1일 00:00부터 현재 시각까지
        LocalDateTime start = LocalDate.now()
            .withDayOfMonth(1)
            .atStartOfDay();
        LocalDateTime end = LocalDateTime.now();

        // 2) DB에서 사용자 로그 조회
        List<EmotionLog> logs = emotionLogRepository
            .findByUserIdAndCreatedAtBetween(userId, start, end);

        // 3) 전체 개수
        long total = logs.size();
        if (total == 0) {
            // 로그가 없으면 모두 0 반환
            return EmotionStatsDto.builder()
                .joy(0.0)
                .sadness(0.0)
                .anger(0.0)
                .calm(0.0)
                .anxiety(0.0)
                .build();
        }

        // 4) 감정별 카운트 집계
        Map<EmotionType, Long> counts = logs.stream()
            .collect(Collectors.groupingBy(EmotionLog::getEmotion, Collectors.counting()));

        // 5) 비율 계산
        double joy     = counts.getOrDefault(EmotionType.joy, 0L)     / (double) total;
        double sadness = counts.getOrDefault(EmotionType.sadness, 0L) / (double) total;
        double anger   = counts.getOrDefault(EmotionType.anger, 0L)   / (double) total;
        double calm    = counts.getOrDefault(EmotionType.calm, 0L)    / (double) total;
        double anxiety = counts.getOrDefault(EmotionType.anxiety, 0L) / (double) total;

        // 6) DTO 반환
        return EmotionStatsDto.builder()
            .joy(joy)
            .sadness(sadness)
            .anger(anger)
            .calm(calm)
            .anxiety(anxiety)
            .build();
    }
}
