package com.example.emotion_analysis.repository;

import com.example.emotion_analysis.entity.EmotionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionLogRepository extends JpaRepository<EmotionLog, Long> {
}
