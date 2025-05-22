package com.example.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDto {
    @Schema(description = "댓글 ID", example = "100")
    private Long id;
    @Schema(description = "게시글 ID", example = "10")
    private Long postId;
    @Schema(description = "작성자 ID", example = "42")
    private Long userId;
    @Schema(description = "댓글 내용", example = "재밌네요!")
    private String content;
    @Schema(description = "생성 시각", example = "2025-05-22T00:00:00")
    private LocalDateTime createdAt;
}