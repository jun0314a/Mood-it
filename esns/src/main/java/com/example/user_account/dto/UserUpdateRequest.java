package com.example.user_account.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    @NotBlank(message = "이름은 필수 입력입니다.")
    private String username;

    @Schema(description = "프로필 이미지 파일", type = "string", format = "binary", required = false)
    private MultipartFile profileImage;
}
