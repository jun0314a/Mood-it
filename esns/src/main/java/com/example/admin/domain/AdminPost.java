package com.example.admin.domain;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "Post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Post_id")
    private Long id;

    @Column(nullable = true)
    private Long boardId;


    @Column(name = "User_id", nullable = false)
    private Long userId;

    @Column(name = "Title", nullable = false, length = 255)
    private String title;

    @Column(name = "Content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "Author", nullable = false, length = 100)
    private String author;

    @Column(name = "Likes", nullable = false)
    private Integer likeCount;

    @Column(name = "Created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    @Column(name = "image_url")
    private String imageUrl;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.likeCount == null) {
            this.likeCount = 0;
        }
    }
}
