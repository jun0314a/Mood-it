// src/main/java/com/example/Group/dto/PostCreateRequest.java
package com.example.group.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {
    private Long authorId;      // Users.user_id
    private String authorName;  // Author VARCHAR(20)
    private String title;
    private String content;
    private String tag;         // Tag VARCHAR(100)
}
