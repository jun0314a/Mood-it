package com.example.story.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "story")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id")
    private Long id;

    private Long userId;

    @Column(length = 1000)
    private String text;

    @Column(name = "picture")
    private String imageUrl;

    @Column(name = "updated_at")
private LocalDateTime updatedAt;

    @Column(name = "timestamp")
    private LocalDateTime createdAt = LocalDateTime.now();

    private int likeCount = 0;
}
