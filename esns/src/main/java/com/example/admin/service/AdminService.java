package com.example.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.admin.dto.PostResponse;
import com.example.admin.dto.UserResponse;
import com.example.admin.repository.AdminPostRepository;
import com.example.admin.repository.AdminUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminUserRepository userRepository;
    private final AdminPostRepository postRepository;

    // ✅ 모든 사용자 조회
    public List<UserResponse> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getRole()))
                .collect(Collectors.toList());
    }

    // ✅ 특정 사용자 삭제
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ✅ 모든 게시글 조회
    public List<PostResponse> findAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getAuthor()))
                .collect(Collectors.toList());
    }

    // ✅ 특정 게시글 삭제
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
