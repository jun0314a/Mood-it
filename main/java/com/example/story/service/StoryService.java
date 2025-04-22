package com.example.story.service;

import com.example.common.exception.BadRequestException;
import com.example.common.util.SwearFilterUtil;
import com.example.story.dto.StoryRequestDto;
import com.example.story.entity.Story;
import com.example.story.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;

    public Story createStory(StoryRequestDto requestDto) {
        if (SwearFilterUtil.containsSwearWords(requestDto.getText())) {
            throw new BadRequestException("욕설이 포함되어 있어요! 다른 표현으로 바꿔보면 어떨까요?");
        }

        Story story = new Story();
        story.setUserId(requestDto.getUserId());
        story.setText(requestDto.getText());
        story.setImageUrl(requestDto.getImageUrl());
        story.setCreatedAt(LocalDateTime.now());
        story.setUpdatedAt(LocalDateTime.now());

        return storyRepository.save(story);
    }

    public List<Story> getRecentStories() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        return storyRepository.findByCreatedAtAfter(cutoff);
    }

    public void likeStory(Long storyId) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new BadRequestException("스토리를 찾을 수 없어요."));
        story.setLikeCount(story.getLikeCount() + 1);
        storyRepository.save(story);
    }

    public void deleteStory(Long storyId) {
        if (!storyRepository.existsById(storyId)) {
            throw new BadRequestException("스토리를 찾을 수 없어요.");
        }
        storyRepository.deleteById(storyId);
    }

    public Story updateStory(Long storyId, StoryRequestDto requestDto) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new BadRequestException("스토리를 찾을 수 없어요."));

        String text = requestDto.getText();
        String imageUrl = requestDto.getImageUrl();

        if ((text == null || text.isBlank()) && (imageUrl == null || imageUrl.isBlank())) {
            throw new BadRequestException("수정할 내용이 없어요!");
        } // 두 곳이 빈칸이면 안됨

        if (SwearFilterUtil.containsSwearWords(text)) {
            throw new BadRequestException("욕설이 포함되어 있어요! 다른 표현으로 바꿔보면 어떨까요?");
        } // 욕설 필터링

        story.setText(text);
        if (imageUrl != null) {
            story.setImageUrl(imageUrl);
        }
        story.setUpdatedAt(LocalDateTime.now());
        story.setLikeCount(0);

        return storyRepository.save(story);
    }
}