package com.example.admin.mapper;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")  // 기본 URL을 /posts로 설정
public class PostMapper {

    public String getAllPosts() {
        return "모든 게시글 가져오기";
    }

    public String createPost() {
        return "게시글 생성";
    }
}
