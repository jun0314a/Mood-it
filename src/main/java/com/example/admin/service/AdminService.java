package com.example.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.admin.dto.PostResponse;
import com.example.admin.dto.UserResponse;
import com.example.admin.repository.AdminPostRepository;
import com.example.admin.repository.AdminUserRepository;
import com.example.admin.mapper.PostMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminUserRepository userRepository;
    private final AdminPostRepository postRepository;

    @Transactional(readOnly = true)
    public List<UserResponse> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getRole()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findAllPosts() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(PostMapper::toPostResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}