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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "모든 사용자 조회", description = "등록된 모든 사용자 정보를 조회합니다.")
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return adminService.findAllUsers();
    }

    @Operation(summary = "사용자 삭제", description = "ID에 해당하는 사용자를 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "해당 사용자 없음")
    })
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
    }

    @Operation(summary = "모든 게시글 조회", description = "등록된 모든 게시글을 조회합니다.")
    @GetMapping("/posts")
    public List<PostResponse> getAllPosts() {
        return adminService.findAllPosts();
    }

    @Operation(summary = "게시글 삭제", description = "ID에 해당하는 게시글을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "해당 게시글 없음")
    })
    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        adminService.deletePost(id);
    }
}