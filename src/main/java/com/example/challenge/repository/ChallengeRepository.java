package com.example.challenge.repository;

import com.example.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    // 감정 태그로 조회
    List<Challenge> findByEmotionTag(String emotionTag);
    
    // 전체 조회 (랜덤 추출을 위해)
    List<Challenge> findAll();
}
