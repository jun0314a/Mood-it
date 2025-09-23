package com.example.admin.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private String author;
    private Long userId;
    private MultipartFile image;
}
