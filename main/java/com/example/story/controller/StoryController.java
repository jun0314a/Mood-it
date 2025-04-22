package com.example.story.controller;

import com.example.story.dto.StoryRequestDto;
import com.example.story.dto.StoryResponseDto;
import com.example.story.entity.Story;
import com.example.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @PostMapping
    public ResponseEntity<StoryResponseDto> uploadStory(@RequestBody StoryRequestDto requestDto) {
        Story story = storyService.createStory(requestDto);
        return ResponseEntity.ok(new StoryResponseDto(story));
    }

    @GetMapping
    public ResponseEntity<List<StoryResponseDto>> getRecentStories() {
        List<Story> stories = storyService.getRecentStories();
        List<StoryResponseDto> response = stories.stream()
                .map(StoryResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    } 

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeStory(@PathVariable Long id) {
        storyService.likeStory(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
        storyService.deleteStory(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoryResponseDto> updateStory(
            @PathVariable Long id,
            @RequestBody StoryRequestDto requestDto) {

        Story updated = storyService.updateStory(id, requestDto);
        return ResponseEntity.ok(new StoryResponseDto(updated));
    }
}