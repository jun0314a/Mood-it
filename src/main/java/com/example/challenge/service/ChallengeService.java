package com.example.challenge.service;

import com.example.challenge.dto.ChallengeRequestDto;   // ← 추가
import com.example.challenge.dto.ChallengeResponseDto;
import com.example.challenge.entity.Challenge;
import com.example.challenge.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;

    // 1) 챌린지 생성 메서드 추가
    @Transactional
    public ChallengeResponseDto createChallenge(ChallengeRequestDto requestDto) {
        Challenge entity = Challenge.builder()
            .title(requestDto.getTitle())
            .description(requestDto.getDescription())
            .emotionTag(requestDto.getEmotionTag())
            .startAt(requestDto.getStartAt())
            .endAt(requestDto.getEndAt())
            .build();
        Challenge saved = challengeRepository.save(entity);
        return mapToDto(saved);
    }

    // 2) 감정 기반 조회
    @Transactional(readOnly = true)
    public List<ChallengeResponseDto> getChallengesByEmotion(String emotionTag) {
        return challengeRepository.findByEmotionTag(emotionTag).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    // 3) 전체 조회
    @Transactional(readOnly = true)
    public List<ChallengeResponseDto> getAllChallenges() {
        return challengeRepository.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    // 4) 중복 없이 랜덤 n개 선택
    @Transactional(readOnly = true)
    public List<ChallengeResponseDto> getRandomChallenges(int count) {
        List<Challenge> all = challengeRepository.findAll();
        Collections.shuffle(all);
        return all.stream()
                  .limit(count)
                  .map(this::mapToDto)
                  .collect(Collectors.toList());
    }

    // Entity → DTO 매핑 (한 번만 정의)
    private ChallengeResponseDto mapToDto(Challenge c) {
        return ChallengeResponseDto.builder()
            .id(c.getId())
            .title(c.getTitle())
            .description(c.getDescription())
            .emotionTag(c.getEmotionTag())
            .startAt(c.getStartAt())
            .endAt(c.getEndAt())
            .build();
    }
}
