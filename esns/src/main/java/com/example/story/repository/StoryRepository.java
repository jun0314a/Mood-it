package com.example.story.repository;

import com.example.story.entity.Story;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByCreatedAtAfter(LocalDateTime cutoff);
}
