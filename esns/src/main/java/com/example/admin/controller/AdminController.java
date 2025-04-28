package com.example.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.admin.dto.PostResponse;
import com.example.admin.dto.UserResponse;
import com.example.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ✅ 모든 사용자 조회
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return adminService.findAllUsers();
    }

    // ✅ 특정 사용자 삭제
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
    }

    // ✅ 모든 게시글 조회
    @GetMapping("/posts")
    public List<PostResponse> getAllPosts() {
        return adminService.findAllPosts();
    }

    // ✅ 특정 게시글 삭제
    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        adminService.deletePost(id);
    }
}
