package com.example.challenge.dto;

import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeResponseDto {
    private Long id;
    private String title;
    private String description;
    private String emotionTag;


    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endAt;
}
