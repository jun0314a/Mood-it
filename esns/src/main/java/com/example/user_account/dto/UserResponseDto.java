package com.example.user_account.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String email;
    private String username;
    private LocalDate birthdate;
    private String phoneNumber;
}
