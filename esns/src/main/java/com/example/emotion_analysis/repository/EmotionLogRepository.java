package com.example.emotion_analysis.repository;

import com.example.emotion_analysis.entity.EmotionLog;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionLogRepository extends JpaRepository<EmotionLog, Long> {

        /**
     * userId와 createdAt 범위를 이용해 로그를 조회합니다.
     */
    List<EmotionLog> findByUserIdAndCreatedAtBetween(
        Long userId,
        LocalDateTime start,
        LocalDateTime end
    );
}
