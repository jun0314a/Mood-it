package com.example.admin.repository;

import java.util.Optional;

import com.example.admin.domain.AdminComment;
import com.example.admin.domain.AdminUserEntity;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUserEntity, Long> {
    // username으로 사용자 찾기
    Optional<AdminUserEntity> findByUsername(String username);
    
    // 특정 username이 존재하는지 확인
}
