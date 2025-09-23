package com.example.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.admin.domain.AdminLikes;

public interface AdminLikesRepository extends JpaRepository<AdminLikes, Long> {
}
