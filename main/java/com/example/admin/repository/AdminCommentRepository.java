package com.example.admin.repository;

import com.example.admin.domain.AdminComment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminCommentRepository extends JpaRepository<AdminComment, Long> {
}
