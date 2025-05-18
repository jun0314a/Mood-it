package com.example.emotion_recommend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "추천 콘텐츠 정보")
public class ContentRecommendationDto {

    @Schema(description = "콘텐츠 타입 (movie, music, book, drama)", example = "movie")
    private String type; // 영화, 음악, 도서, 드라마

    @Schema(description = "콘텐츠 제목", example = "Inception")
    private String title;

    @Schema(description = "콘텐츠 설명", example = "A mind-bending thriller...")
    private String description; // 영화, 드라마는 TMDB에서 제공

    @Schema(description = "이미지 URL", example = "http://.../poster.jpg")
    private String imageUrl;    // 영화, 드라마는 TMDB에서 제공
}
