package com.example.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String author;

    // 직접 구현한 null-safe 생성자만 사용
    public PostResponse(Long id, String title, String content, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = (author != null) ? author : "알 수 없음"; // null-safe 적용부분분 
    }
}

