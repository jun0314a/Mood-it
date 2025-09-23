package com.example.emotion_recommend.service;

import com.example.emotion_analysis.dto.EmotionResultDto;
import com.example.emotion_analysis.service.EmotionService;
import com.example.emotion_recommend.client.TmdbClient;
import com.example.emotion_recommend.dto.ContentRecommendationDto;
import com.example.emotion_recommend.dto.RecommendationResponseDto;
import com.example.emotion_analysis.service.ChatGptClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

        // 2) [변경] 더 똑똑해진 추천 요청 프롬프트: 사용자의 원본 텍스트를 포함하고, 후보를 3개씩 요청합니다.
        String recPrompt = String.format(
                """
                사용자가 남긴 원본 문장은 "%s"이고, 여기서 분석된 핵심 감정은 "%s"입니다.
                이 사용자의 상황과 감정에 어울리는 콘텐츠를 추천하려고 합니다.
                아래 JSON 형식에 맞춰, 각 카테고리별로 추천 콘텐츠 후보를 **3개씩** 배열로 제안해주세요.
                추가 설명 없이, valid JSON 객체만 반환해야 합니다.
                {
                  "movie": ["영화1", "영화2", "영화3", "영화4", "영화5", "영화6", "영화7", "영화8", "영화9", "영화10"],
                  "drama": ["드라마1", "드라마2", "드라마3", "드라마4", "드라마5", "드라마6", "드라마7", "드라마8", "드라마9", "드라마10"],
                  "music": ["음악1", "음악2", "음악3", "음악4", "음악5", "음악6", "음악7", "음악8", "음악9", "음악10"],
                  "book": ["책1", "책2", "책3", "책4", "책5", "책6", "책7", "책8", "책9", "책10"]
                }
                """, text, emotion // 원본 텍스트와 감정을 모두 프롬프트에 사용
        );

        List<Map<String,String>> recMsgs = List.of(
            Map.of(
                "role","system",
                "content","당신은 감정 기반 콘텐츠 추천 전문가입니다. 반드시 한글로만 제목을 추천하고, valid JSON 객체만 반환하세요."
            ),
            Map.of("role","user","content",recPrompt)
        );

        // 3) [변경] GPT 호출 시 temperature를 높여 창의적인 답변 유도
        String rawJson = chatGptClient.chatCompletion(recMsgs, 0.8); // 창의성 0.8로 상향!
        logger.debug("Raw recommendation JSON from GPT: {}", rawJson);
        String json = extractJson(rawJson);

        // 4) [변경] JSON 파싱: 이제 추천 제목이 List<String> 형태입니다.
        Map<String, List<String>> candidateMap;
        try {
            candidateMap = objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception ex) {
            logger.error("추천 JSON 파싱 실패. rawJson={}", rawJson, ex);
            throw new RuntimeException("추천 JSON 파싱 실패: " + rawJson, ex);
        }

        // 5) [변경] TMDB/GPT 로직: 여러 후보 중 하나를 '랜덤'으로 선택해서 상세 정보를 조회합니다.
        List<ContentRecommendationDto> list = new ArrayList<>();
        Random random = new Random(); // 랜덤 선택을 위한 Random 객체 생성

        for (var e : candidateMap.entrySet()) {
            String type = e.getKey();
            List<String> candidates = e.getValue(); // 3개의 후보 리스트

            if (candidates == null || candidates.isEmpty()) {
                continue; // 후보가 없으면 건너뛰기
            }

            // N개의 후보 중 하나를 무작위로 선택!
            String title = candidates.get(random.nextInt(candidates.size()));
            logger.debug("Category '{}' candidates: {}. Selected: '{}'", type, candidates, title);


            // 선택된 title로 상세 정보 조회 (이하 로직은 거의 동일)
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
                String description = chatGptClient.chatCompletion(descMsgs, 0.0); // 설명은 간결해야 하므로 temperature는 0.0 유지
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
