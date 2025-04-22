package com.example.story.scheduler;

import com.example.story.repository.StoryRepository;
import com.example.story.entity.Story;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoryCleanupScheduler {

    private final StoryRepository storyRepository;

    // 매 시간마다 실행 (cron = "0 0 * * * *")
    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredStories() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        List<Story> oldStories = storyRepository.findAll().stream()
                .filter(story -> story.getCreatedAt().isBefore(cutoff))
                .toList();

        if (!oldStories.isEmpty()) {
            storyRepository.deleteAll(oldStories);
            log.info("{}개의 오래된 스토리가 삭제되었습니다.", oldStories.size());
        }
    }
}
