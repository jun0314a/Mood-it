package com.example.user_account.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Builder
@Schema(description = "회원 가입 후 반환되는 사용자 정보")
public class UserResponseDto {

    @Schema(description = "사용자 고유 ID", example = "123")
    private Long id;

    @Schema(description = "이메일", example = "user@example.com")
    private String email;

    @Schema(description = "닉네임", example = "TestUser")
    private String username;

    @Schema(
      description = "생년월일",
      example = "1990-01-01",
      type = "string", format = "date"
    )   
    private LocalDate birthdate;

    @Schema(description = "전화번호", example = "01012345678")
    private String phoneNumber;

    @Schema(
    description = "프로필 이미지 URL",
    example = "http://example.com/images/profile.png",
    format = "uri")
    private String profileImageUrl;
}
