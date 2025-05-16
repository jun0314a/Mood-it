package com.example.emotion_recommend.service;

import com.example.emotion_analysis.dto.EmotionResultDto;
import com.example.emotion_analysis.service.EmotionService;
import com.example.emotion_recommend.client.TmdbClient;
import com.example.emotion_recommend.dto.ContentRecommendationDto;
import com.example.emotion_recommend.dto.RecommendationResponseDto;
import com.example.emotion_analysis.service.ChatGptClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    private final EmotionService emotionService;
    private final ChatGptClient chatGptClient;
    private final TmdbClient tmdbClient;
    private final ObjectMapper objectMapper;

    /**
     * 1) text로 감정 분석 → DB 저장
     * 2) 분석된 대표 감정으로 콘텐츠 추천 → 최종 DTO 반환
     */
    public RecommendationResponseDto getRecommendationsByText(String text, Long userId) {
        // 1) 감정 분석 및 DB 저장
        EmotionResultDto er = emotionService.analyzeAndSave(text, userId);
        String emotion = er.getTopEmotion().name().toLowerCase();
        logger.debug("Detected and saved emotion: {}", emotion);

        // 2) 추천 제목용 JSON 스키마 프롬프트
        String recPrompt = String.format(
            "사용자 감정: \"%s\"\n" +
            "아래 JSON 스키마에 맞춰, 추천할 **한글 제목**만 채워서 반환하세요. 추가 텍스트 금지.\n" +
            "{\n" +
            "  \"movie\": \"영화제목\",\n" +
            "  \"drama\": \"드라마제목\",\n" +
            "  \"book\": \"책제목\",\n" +
            "  \"music\": \"음악제목\"\n" +
            "}", emotion
        );
        List<Map<String,String>> recMsgs = List.of(
            Map.of(
                "role","system",
                "content","당신은 감정 기반 콘텐츠 추천 전문가입니다. 반드시 한글로만 제목을 추천하고, valid JSON 객체만 반환하세요."
            ),
            Map.of("role","user","content",recPrompt)
        );

        // 3) GPT 호출 → 순수 JSON 문자열 추출
        String rawJson = chatGptClient.chatCompletion(recMsgs, 0.0);
        logger.debug("Raw recommendation JSON: {}", rawJson);
        String json = extractJson(rawJson);

        // 4) JSON → Map<type, title>
        Map<String,String> titles;
        try {
            titles = objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception ex) {
            logger.error("추천 JSON 파싱 실패. rawJson={}", rawJson, ex);
            throw new RuntimeException("추천 JSON 파싱 실패: " + rawJson, ex);
        }

        // 5) TMDB/GPT 로직으로 ContentRecommendationDto 리스트 생성
        List<ContentRecommendationDto> list = new ArrayList<>();
        for (var e : titles.entrySet()) {
            String type = e.getKey();
            String title = e.getValue();

            if ("movie".equalsIgnoreCase(type) || "drama".equalsIgnoreCase(type)) {
                var detail = tmdbClient.searchContentByTitle(title);
                list.add(new ContentRecommendationDto(
                    type,
                    title,
                    detail.getDescription() != null ? detail.getDescription() : "설명 없음",
                    detail.getImageUrl()
                ));
            } else {
                String descPrompt = String.format("‘%s’라는 %s에 대한 한 문장 설명(최대 30자)만 한글로 해주세요.", title, type);
                List<Map<String,String>> descMsgs = List.of(
                    Map.of("role","system","content","간결한 한 문장 설명만 반환하세요. 추가 텍스트 금지."),
                    Map.of("role","user","content",descPrompt)
                );
                String description = chatGptClient.chatCompletion(descMsgs, 0.0);
                list.add(new ContentRecommendationDto(type, title, description, null));
            }
        }

        return new RecommendationResponseDto(emotion, list);
    }

    /** rawText에서 처음 '{'부터 마지막 '}' 사이만 잘라내 반환 */
    private String extractJson(String rawText) {
        int start = rawText.indexOf('{');
        int end = rawText.lastIndexOf('}');
        if (start != -1 && end > start) {
            return rawText.substring(start, end + 1);
        }
        return rawText;
    }
}
