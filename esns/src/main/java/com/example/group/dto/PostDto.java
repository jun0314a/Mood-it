// src/main/java/com/example/Group/dto/PostDto.java
package com.example.group.dto;

import lombok.*;
import java.time.LocalDateTime;

import com.example.group.entity.PostType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private Integer postId;
    private Long userId;
    private String authorName;
    private Integer boardId;
    private Integer groupId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer likes;
    private String tag;
    private PostType postType;
}
