package com.example.admin.service;

import com.example.admin.dto.CommentCreateRequest;
import com.example.admin.dto.CommentDto;
import java.util.List;

public interface CommentService {
    List<CommentDto> getCommentsByPost(Long postId);
    CommentDto createComment(Long postId, CommentCreateRequest request);
    void deleteComment(Long postId, Long commentId);
}