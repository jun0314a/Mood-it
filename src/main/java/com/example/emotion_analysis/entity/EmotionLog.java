package com.example.emotion_analysis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emotionlog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmotionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(nullable = false)
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    private EmotionType emotion;

    private LocalDateTime createdAt;
}
