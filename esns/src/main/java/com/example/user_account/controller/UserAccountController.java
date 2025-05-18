package com.example.user_account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth.jwt.JwtUtil;
import com.example.user_account.dto.UserResponseDto;
import com.example.user_account.dto.UserSignupRequest;
import com.example.user_account.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(
      summary = "회원 가입",
      description = "이메일/비밀번호/이름/생년월일/전화번호를 받아 신규 회원을 생성합니다."
    )
    @ApiResponses({
      @ApiResponse(
        responseCode = "201",
        description = "가입 성공",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = UserResponseDto.class),
          examples = @ExampleObject(value = "{\"id\":1,\"email\":\"user@ex.com\",\"username\":\"홍길동\",\"birthdate\":\"1990-01-01\",\"phoneNumber\":\"010-1234-5678\"}")
        )
      ),
      @ApiResponse(responseCode = "400", description = "입력 검증 오류")
    })
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody UserSignupRequest request) {
        UserResponseDto userResponse = userService.signup(request);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후 토큰 부분 추출
        }
        throw new IllegalArgumentException("Authorization 헤더가 유효하지 않습니다.");
    }

    @Operation(
      summary = "회원 탈퇴",
      description = "헤더의 Bearer JWT 토큰을 읽어 해당 회원을 삭제합니다."
    )
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "탈퇴 완료"),
      @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        String email = jwtUtil.extractEmail(token);
        userService.deleteUserByEmail(email);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
    
}
