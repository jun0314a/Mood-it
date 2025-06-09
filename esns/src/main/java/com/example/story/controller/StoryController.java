package com.example.story.controller;

import com.example.story.dto.StoryRequestDto;
import com.example.story.dto.StoryResponseDto;
import com.example.story.entity.Story;
import com.example.story.service.StoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @Operation(summary = "스토리 업로드", description = "텍스트/이미지 URL을 받아 새 스토리를 저장합니다.")
    @ApiResponses({
      @ApiResponse(responseCode="200", description="업로드 성공",
        content=@Content(schema=@Schema(implementation=StoryResponseDto.class))
      )
    })
    @PostMapping
    public ResponseEntity<StoryResponseDto> uploadStory(@RequestBody StoryRequestDto requestDto) {
        Story story = storyService.createStory(requestDto);
        return ResponseEntity.ok(new StoryResponseDto(story));
    }

    @Operation(summary = "최신 스토리 조회", description = "최근 스토리 목록을 반환합니다.")
    @ApiResponses({
      @ApiResponse(responseCode="200", description="조회 성공",
        content=@Content(array=@ArraySchema(schema=@Schema(implementation=StoryResponseDto.class)))
      )
    })
    @GetMapping
    public ResponseEntity<List<StoryResponseDto>> getRecentStories() {
        List<Story> stories = storyService.getRecentStories();
        List<StoryResponseDto> response = stories.stream()
                .map(StoryResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    } 

    @Operation(summary = "좋아요 추가", description = "해당 스토리의 좋아요 수를 1 증가시킵니다.")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "좋아요 추가 성공",
        content = @Content  // 반환 바디 없음
      )
    })
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeStory(@PathVariable Long id) {
        storyService.likeStory(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스토리 수정", description = "해당 ID의 스토리를 새로운 내용으로 변경합니다.")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수정 성공",
        content = @Content(schema = @Schema(implementation = StoryResponseDto.class))
      )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
        storyService.deleteStory(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스토리 수정", description = "스토리를 수정합니다.")
    @ApiResponses({
      @ApiResponse(responseCode="200", description="수정 성공",
        content=@Content(array=@ArraySchema(schema=@Schema(implementation=StoryResponseDto.class)))
      )
    }) 
    @PutMapping("/{id}")
    public ResponseEntity<StoryResponseDto> updateStory(
            @PathVariable Long id,
            @RequestBody StoryRequestDto requestDto) {

        Story updated = storyService.updateStory(id, requestDto);
        return ResponseEntity.ok(new StoryResponseDto(updated));
    }
}