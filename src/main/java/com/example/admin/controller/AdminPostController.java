package com.example.admin.controller;

import com.example.admin.dto.CommentCreateRequest;
import com.example.admin.dto.CommentDto;
import com.example.admin.dto.PostRequest;
import com.example.admin.dto.PostResponse;
import com.example.admin.service.PostService;
import com.example.admin.service.CommentService;
import com.example.auth.service.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
@Tag(name = "게시글 API", description = "게시글 생성, 조회, 수정, 삭제, 좋아요 기능을 제공하는 API입니다.")
public class AdminPostController {

    private final PostService postService;
    private final CommentService commentService;

    @Operation(summary = "게시글 생성", description = "이미지를 포함할 수 있는 게시글을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createPost(@ModelAttribute PostRequest postRequest) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();

        postRequest.setUserId(currentUserId);

        Long postId = postService.savePost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }
    
    @Operation(summary = "게시글 좋아요", description = "특정 게시글에 좋아요를 누릅니다.")
    @ApiResponse(responseCode = "200", description = "좋아요 성공")
    @PostMapping("/{id}/like")
    public ResponseEntity<String> likePost(@PathVariable Long id) {
        postService.likePost(id);
        return ResponseEntity.ok("좋아요 완료");
    }

    @Operation(summary = "게시글 상세 조회", description = "ID로 게시글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = PostResponse.class)))
    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Long id) {
        return postService.findPostById(id);
    }

    @Operation(summary = "전체 게시글 조회", description = "모든 게시글 목록을 최신순으로 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PostResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "게시글 수정", description = "ID에 해당하는 게시글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePost(@PathVariable Long id, @ModelAttribute PostRequest postRequest) throws IOException {
        Long updatedPostId = postService.updatePost(id, postRequest);
        return ResponseEntity.ok(updatedPostId);
    }

    @Operation(summary = "게시글 삭제", description = "ID에 해당하는 게시글을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @Operation(summary = "댓글 목록 조회", description = "게시글에 달린 모든 댓글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentDto.class))))
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long postId) {
        List<CommentDto> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long postId,
            @RequestBody CommentCreateRequest request) {
        CommentDto created = commentService.createComment(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    @DeleteMapping("/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        commentService.deleteComment(postId, commentId);
    }
}