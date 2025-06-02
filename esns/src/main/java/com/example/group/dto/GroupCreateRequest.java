package com.example.group.dto;

import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCreateRequest {

    /** 이미지 파일 (multipart/form-data로만 수신됨, JSON에는 포함되지 않음) */
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
    private String emotion;

    /** 서버에서 할당된 이미지 URL (응답 용도) */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String profileImageUrl;
}