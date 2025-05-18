package com.example.story.dto;

import com.example.story.entity.Story;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "스토리 조회 응답 정보")
public class StoryResponseDto {

    @Schema(description = "스토리 고유 ID", example = "456")
    private final Long id;

    @Schema(description = "작성자 사용자 ID", example = "123")
    private final Long userId;

    @Schema(description = "스토리 본문 텍스트", example = "나랑 놀러가실 분!")
    private final String text;

    @Schema(description = "이미지 URL (선택)", example = "http://.../image.jpg")
    private final String imageUrl;

    @Schema(description = "생성 시각", example = "2025-05-09T15:00:00")
    private final LocalDateTime createdAt;

    @Schema(description = "수정 시각", example = "2025-05-09T15:05:00")
    private final LocalDateTime updatedAt;

    @Schema(description = "좋아요 수", example = "99")
    private final int likeCount;

    public StoryResponseDto(Story story) {
        this.id = story.getId();
        this.userId = story.getUserId();
        this.text = story.getText();
        this.imageUrl = story.getImageUrl();
        this.createdAt = story.getCreatedAt();
        this.updatedAt = story.getUpdatedAt();
        this.likeCount = story.getLikeCount();
    }
}