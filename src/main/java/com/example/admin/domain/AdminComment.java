package com.example.admin.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
// 🚨🚨 JPA 컨벤션에 따라 테이블명은 소문자 복수형을 사용합니다.
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 🚨🚨 JPA Auditing을 활성화하여 생성/수정일 관리를 자동화합니다.
@EntityListeners(AuditingEntityListener.class)
public class AdminComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 🚨🚨 JPA 컨벤션에 따라 컬럼명은 소문자와 언더스코어를 사용합니다.
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private AdminPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AdminUserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private AdminComment parentComment;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    // 🚨🚨 @CreatedDate와 @LastModifiedDate로 자동 관리합니다.
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    // 🚨🚨🚨 누락된 updatedAt 필드를 추가합니다.
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 'likes' 컬럼명도 소문자로 변경
    @Column(name = "likes", nullable = false)
    private int likes;
}