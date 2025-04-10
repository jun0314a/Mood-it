package com.example.user_account.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String email;
    private String username;
    private String birthdate;
    private String phoneNumber;
}
