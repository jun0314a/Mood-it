package com.example.story.dto;

import com.example.story.entity.Story;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StoryResponseDto {
    private final Long id;
    private final Long userId;
    private final String text;
    private final String imageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
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