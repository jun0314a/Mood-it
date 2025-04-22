package com.example.user_account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupRequest {

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,}$",
        message = "비밀번호는 최소 8자 이상이며, 영문 대/소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다."
    )
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String username;

    @NotNull(message = "생년월일은 필수 입력 항목입니다.")
    private LocalDate birthdate;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    private String phoneNumber;
}
