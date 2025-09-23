package com.example.admin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String author;
    private String imageUrl;
    
    private int likes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentDto> comments;
}