package com.example.admin.mapper;

import com.example.admin.domain.AdminPost;
import com.example.admin.dto.PostResponse;
import com.example.admin.dto.CommentDto;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {
    public static PostResponse toPostResponse(AdminPost post) {
        if (post == null) {
            return null;
        }

        List<CommentDto> commentDtos = null;
        if (post.getComments() != null) {
            commentDtos = post.getComments().stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList());
        }

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                // 🚨 수정: User 엔티티에서 작성자 이름을 가져옴
                .author(post.getUser().getUsername())
                .imageUrl(post.getImageUrl())
                .likes(post.getLikes())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .comments(commentDtos)
                .build();
    }
}