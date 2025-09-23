// src/main/java/com/example/group/dto/GroupCreateRequest.java
package com.example.group.dto;

import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import jakarta.validation.constraints.NotBlank; // 🚨 이 임포트가 정확히 있는지 확인

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCreateRequest {

    @JsonIgnore
    private MultipartFile image;

    private Long creatorId;
    /** 그룹 제목 */
    private String title;
    /** 그룹 설명 */
    private String description;
    /** 콤마로 구분된 태그 목록 */
    private String tags;

    /** 감정 태그 (joy, sadness, anger, calm, anxiety) */
    @NotBlank(message = "감정 태그는 필수 입력 항목입니다.") // 🚨 이 라인이 정확히 있는지 확인
    private String emotion;
}