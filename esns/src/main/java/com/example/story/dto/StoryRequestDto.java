package com.example.story.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoryRequestDto {
    private Long userId;
    private String text;
    private String imageUrl;
}