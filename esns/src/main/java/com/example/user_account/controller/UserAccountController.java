package com.example.user_account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.auth.jwt.JwtUtil;
import com.example.user_account.dto.UserResponseDto;
import com.example.user_account.dto.UserSignupRequest;
import com.example.user_account.service.UserService;
import com.example.user_account.dto.UserUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
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

    private String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new IllegalArgumentException("Authorization 헤더가 유효하지 않습니다.");
    }

    @Operation(
      summary = "회원 가입",
      description = "이메일/비밀번호/이름/생년월일/전화번호와 프로필 이미지를 받아 신규 회원을 생성합니다."
    )
    @ApiResponses({
      @ApiResponse(
        responseCode = "201",
        description = "가입 성공",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = UserResponseDto.class),
          examples = @ExampleObject(value = "{\"id\":1,\"email\":\"user@ex.com\",\"username\":\"홍길동\",\"birthdate\":\"1990-01-01\",\"phoneNumber\":\"010-1234-5678\",\"profileImageUrl\":\"/profile-images/uuid.png\"}")
        )
      ),
      @ApiResponse(responseCode = "400", description = "입력 검증 오류")
    })
    @PostMapping(
      value = "/signup",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UserResponseDto> signup(
            @RequestPart("data") @Valid UserSignupRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        request.setProfileImage(profileImage);
        UserResponseDto userResponse = userService.signup(request);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
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

    @Operation(
      summary = "회원 프로필 조회",
      description = "경로 변수로 받은 회원 ID의 상세 정보를 조회합니다."
    )
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = UserResponseDto.class)
        )
      ),
      @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getProfile(@PathVariable Long id) {
        UserResponseDto profile = userService.getUserProfile(id);
        return ResponseEntity.ok(profile);
    }

    @Operation(
      summary = "회원 정보 수정",
      description = "경로 변수로 받은 회원 ID의 이름·프로필 이미지를 수정합니다."
    )
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수정 성공",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = UserResponseDto.class)
        )
      ),
      @ApiResponse(responseCode = "400", description = "입력 검증 오류"),
      @ApiResponse(responseCode = "401", description = "권한 없음"),
      @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음")
    })
    @PutMapping(
      value = "/{id}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UserResponseDto> updateProfile(
            @PathVariable Long id,
            @RequestPart("data") @Valid UserUpdateRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            HttpServletRequest httpRequest
    ) {
        // JWT 검증 → 토큰에서 얻은 사용자 이메일과 경로변수 ID의 사용자 이메일 비교
        String token = extractTokenFromHeader(httpRequest);
        String email = jwtUtil.extractEmail(token);
        userService.verifyEmailMatchesId(email, id);

        request.setProfileImage(profileImage);
        UserResponseDto updated = userService.updateUser(id, request);
        return ResponseEntity.ok(updated);
    }
}
