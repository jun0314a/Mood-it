package com.example.user_account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 회원가입 요청 정보")
public class UserSignupRequest {

    @Schema(
        description = "회원 이메일 (로그인 시 사용)",
        example = "user@example.com",
        required = true
    )
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

    @Schema(
        description = "비밀번호 (최소 8자, 영문 대/소문자·숫자·특수문자 포함)",
        example = "Abcdef1!",
        required = true
    )
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,}$",
        message = "비밀번호는 최소 8자 이상이며, 영문 대/소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다."
    )
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;

    @Schema(
        description = "사용자 이름 (닉네임)",
        example = "HappyUser",
        required = true
    )
    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String username;

    @Schema(
        description = "생년월일 (YYYY-MM-DD)",
        example = "1990-01-01",
        required = true,
        type = "string",
        format = "date"
    )
    @NotNull(message = "생년월일은 필수 입력 항목입니다.")
    private LocalDate birthdate;

    @Schema(
        description = "전화번호 (하이픈 없이 숫자만)",
        example = "01012345678",
        required = true
    )
    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    private String phoneNumber;
}