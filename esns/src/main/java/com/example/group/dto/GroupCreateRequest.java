package com.example.group.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCreateRequest {
    private Long creatorId;
    /** 그룹 제목 */
    private String title;
    /** 그룹 설명 */
    private String description;
    /** 콤마로 구분된 태그 목록 */
    private String tags;
    /** 감정 태그 (joy, sadness, anger, calm, anxiety) */
    private String emotion;
}