package com.example.admin.mapper;

import com.example.admin.domain.AdminComment;
import com.example.admin.dto.CommentDto;

public class CommentMapper {
    public static CommentDto toCommentDto(AdminComment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorId(comment.getUser().getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}