package com.example.admin.repository;

import com.example.admin.domain.AdminComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdminCommentRepository extends JpaRepository<AdminComment, Long> {

    /**
     * AdminComment.post.id (== AdminPost.id) 기준으로
     * createdAt 오름차순으로 댓글을 조회합니다.
     */
    List<AdminComment> findByPost_IdOrderByCreatedAtAsc(Long postId);
}