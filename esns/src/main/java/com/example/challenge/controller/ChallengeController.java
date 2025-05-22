package com.example.challenge.controller;

import com.example.challenge.dto.ChallengeRequestDto;
import com.example.challenge.dto.ChallengeResponseDto;
import com.example.challenge.service.ChallengeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Challenge", description = "챌린지 관련 기능")
@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    @Operation(summary = "챌린지 생성", description = "새로운 챌린지를 생성합니다.")
    @PostMapping
    public ResponseEntity<ChallengeResponseDto> createChallenge(
        @RequestBody ChallengeRequestDto requestDto
    ) {
        return ResponseEntity.ok(challengeService.createChallenge(requestDto));
    }

    @Operation(summary = "감정 기반 추천", description = "특정 감정 태그에 맞는 챌린지를 추천합니다.")
    @GetMapping("/recommend")
    public ResponseEntity<List<ChallengeResponseDto>> recommendByEmotion(
        @RequestParam String emotionTag
    ) {
        return ResponseEntity.ok(challengeService.getChallengesByEmotion(emotionTag));
    }

    @Operation(summary = "전체 챌린지 조회", description = "등록된 모든 챌린지를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ChallengeResponseDto>> getAllChallenges() {
        return ResponseEntity.ok(challengeService.getAllChallenges());
    }

    @Operation(summary = "랜덤 챌린지 하나 조회", description = "무작위 챌린지 하나를 가져옵니다.")
    @GetMapping("/random")
    public ResponseEntity<ChallengeResponseDto> getRandomChallenge() {
        return ResponseEntity.ok(challengeService.getRandomChallenges(1).get(0));
    }

    @Operation(summary = "랜덤 챌린지 목록 조회", description = "지정한 개수만큼 무작위 챌린지 목록을 가져옵니다.")
    @GetMapping("/random/list")
    public ResponseEntity<List<ChallengeResponseDto>> getRandomChallenges(
            @RequestParam(defaultValue = "3") int count) {
        return ResponseEntity.ok(challengeService.getRandomChallenges(count));
    }
}
