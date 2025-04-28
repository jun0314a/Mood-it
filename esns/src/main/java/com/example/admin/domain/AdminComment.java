package com.example.admin.domain;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Post_id", nullable = false)
    private AdminPost post;

    @ManyToOne
    @JoinColumn(name = "User_id", nullable = false)
    private AdminUserEntity user;

    @ManyToOne
    @JoinColumn(name = "Parent_Comment_id")
    private AdminComment parentComment;

    @Column(name = "Content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "Created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "Likes", nullable = false)
    private int likes;
}
