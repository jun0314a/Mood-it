package com.example.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.admin.dto.PostRequest;
import com.example.admin.dto.PostResponse;
import com.example.admin.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final PostService postService;

    // ✅ 게시글 생성 (이미지 포함 가능)
    @PostMapping
    public ResponseEntity<Long> createPost(@ModelAttribute PostRequest postRequest) {
        try {
            Long postId = postService.savePost(postRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(postId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 게시글 좋아요
    @PostMapping("/{id}/like")
    public ResponseEntity<String> likePost(@PathVariable Long id) {
        postService.likePost(id);
        return ResponseEntity.ok("좋아요 완료");
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Long id) {
        return postService.findPostById(id);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest) {
        return postService.updatePost(id, postRequest);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}
