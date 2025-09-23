package com.example.admin.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
// 🚨🚨 JPA 컨벤션에 따라 테이블명은 소문자 복수형을 사용합니다.
@Table(name = "Comment")
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
    @Column(name = "Comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Post_id", nullable = false)
    private AdminPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id", nullable = false)
    private AdminUserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Parent_Comment_id")
    private AdminComment parentComment;

    @Column(name = "Content", nullable = false, columnDefinition = "TEXT")
    private String content;

    // 🚨🚨 @CreatedDate와 @LastModifiedDate로 자동 관리합니다.
    @CreatedDate
    @Column(name = "Created_at", updatable = false)
    private LocalDateTime createdAt;

    // 스키마에 Updated_at 컬럼이 없을 수 있어, 일단 비영속 필드로 보유
    @Transient
    private LocalDateTime updatedAt;

    // 'likes' 컬럼명도 소문자로 변경
    @Column(name = "Likes", nullable = false)
    private int likes;
}