package com.example.group.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.example.user_account.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Post_id")
    private Integer postId;

    // FK: Users(User_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id", nullable = false)
    private User user;

    // Author VARCHAR(20)
    @Column(name = "Author", length = 20)
    private String authorName;

    // 그룹 게시글인 경우만 사용
    @Column(name = "Group_id")
    private Integer groupId;

    @Column(name = "Title", length = 255, nullable = false)
    private String title;

    @Column(name = "Content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "Created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "Updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "Likes")
    private Integer likes;

    @Column(name = "Tag", length = 100)
    private String tag;

    @Enumerated(EnumType.STRING)
    @Column(name = "Post_type", nullable = false)
    private PostType postType;
}
