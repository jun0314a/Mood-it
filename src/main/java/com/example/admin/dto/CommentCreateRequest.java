package com.example.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    @NotNull(message = "작성자 ID는 필수입니다.")
    @Schema(description = "작성자 ID", example = "42", required = true)
    private Long userId;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Schema(description = "댓글 내용", example = "좋은 글이에요!", required = true)
    private String content;
}