package com.example.emotion_recommend.service;

import com.example.emotion_analysis.service.ChatGptClient;
import com.example.emotion_recommend.client.TmdbClient;
import com.example.emotion_recommend.dto.ContentRecommendationDto;
import com.example.emotion_recommend.dto.RecommendationResponseDto;
import com.example.emotion_recommend.dto.TmdbContentDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final ChatGptClient chatGptClient;
    private final TmdbClient tmdbClient;

    public RecommendationResponseDto getRecommendationsByEmotion(String emotion) {
        String prompt = String.format(
            "사용자의 감정은 '%s'입니다. 해당 감정에 어울리는 **영화, 노래, 책, 드라마**를 각각 하나씩 추천해주세요.\n" +
            "- 추천은 매번 다르게 해주세요.\n" +
            "- 제목은 **반드시 한국어**로 출력해주세요.\n" +
            "- 형식: 영화: {제목} / 노래: {제목} / 책: {제목} / 드라마: {제목}",
            emotion);

        String gptResponse = chatGptClient.getEmotionFromText(prompt);
        String[] recommendations = gptResponse.split("/");

        List<ContentRecommendationDto> contentList = new ArrayList<>();

        for (String recommendation : recommendations) {
            String[] parts = recommendation.split(":");
            if (parts.length < 2) continue;

            String type = parts[0].trim();
            String title = parts[1].trim();

            // 영화나 드라마는 TMDB 사용
            if (type.equalsIgnoreCase("영화") || type.equalsIgnoreCase("드라마")) {
                TmdbContentDetail detail = tmdbClient.searchContentByTitle(title);

                // 설명/이미지 누락 시 재검사 1번
                if (detail.getDescription() == null || detail.getImageUrl() == null) {
                    detail = tmdbClient.searchContentByTitle(title); // 한 번만 재시도
                }

                contentList.add(new ContentRecommendationDto(
                        type,
                        title,
                        detail.getDescription() != null ? detail.getDescription() : "설명 없음",
                        detail.getImageUrl()
                ));
            }
            // 노래와 책은 ChatGPT로 설명 생성, 이미지 없음
            else {
                String subPrompt = String.format("‘%s’라는 %s에 대한 간단한 설명을 1문장으로 해주세요. (최대 30자)", title, type);
                String description = chatGptClient.getEmotionFromText(subPrompt);

                contentList.add(new ContentRecommendationDto(
                        type,
                        title,
                        description,
                        null // 이미지 없음
                ));
            }
        }

        return new RecommendationResponseDto(emotion, contentList);
    }
}
