package com.example.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.admin.domain.AdminPost;

@Repository
public interface AdminPostRepository extends JpaRepository<AdminPost, Long> {
}
