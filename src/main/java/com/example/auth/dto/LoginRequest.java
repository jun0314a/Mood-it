package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "로그인 요청 정보")
public class LoginRequest {

    @Schema(description = "회원 이메일", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "회원 비밀번호", example = "Abcdef1!", required = true)
    private String password;
}
