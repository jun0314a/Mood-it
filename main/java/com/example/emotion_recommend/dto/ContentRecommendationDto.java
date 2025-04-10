package com.example.emotion_recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContentRecommendationDto {
    private String type; // 영화, 음악, 도서, 드라마
    private String title;
    private String description; // 영화, 드라마는 TMDB에서 제공
    private String imageUrl;    // 영화, 드라마는 TMDB에서 제공
}
