package com.example.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
}
