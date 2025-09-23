package com.example.story.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "스토리 생성 요청 정보")
public class StoryRequestDto {

    @Schema(description = "작성자 사용자 ID", example = "123")
    private Long userId;

    @Schema(description = "스토리 본문 텍스트", example = "오늘 기분이 좋네요!")
    private String text;

    @Schema(description = "이미지 URL (선택)", example = "http://.../image.jpg")
    private String imageUrl;
}