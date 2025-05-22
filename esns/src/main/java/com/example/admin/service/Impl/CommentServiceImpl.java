package com.example.admin.service.Impl;

import com.example.admin.dto.CommentCreateRequest;
import com.example.admin.dto.CommentDto;
import com.example.admin.domain.AdminComment;
import com.example.admin.domain.AdminPost;
import com.example.admin.domain.AdminUserEntity;
import com.example.admin.repository.AdminCommentRepository;
import com.example.admin.repository.AdminPostRepository;
import com.example.admin.repository.AdminUserRepository;
import com.example.admin.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final AdminCommentRepository commentRepository;
    private final AdminPostRepository postRepository;
    private final AdminUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByPost(Long postId) {
        List<AdminComment> comments = commentRepository.findByPost_IdOrderByCreatedAtAsc(postId);
        return comments.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long postId, CommentCreateRequest request) {
        AdminPost post = postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. id=" + postId));
        AdminUserEntity user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("사용자가 없습니다. id=" + request.getUserId()));

        AdminComment comment = AdminComment.builder()
            .post(post)
            .user(user)
            .content(request.getContent())
            .createdAt(LocalDateTime.now())
            .build();
        AdminComment saved = commentRepository.save(comment);
        return toDto(saved);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        AdminComment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException("댓글이 없습니다. id=" + commentId));
        if (!comment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("잘못된 게시글 ID 입니다.");
        }
        commentRepository.delete(comment);
    }

    private CommentDto toDto(AdminComment c) {
        return CommentDto.builder()
            .id(c.getId())
            .postId(c.getPost().getId())
            .userId(c.getUser().getId())
            .content(c.getContent())
            .createdAt(c.getCreatedAt())
            .build();
    }
}
