package com.example.group.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * 클라이언트에 반환할 그룹 정보 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDto {
    private Integer groupId;
    private Long creatorId;
    private String title;
    private String description;
    private String tags;
    private String emotion;
    private LocalDateTime createdAt;
}