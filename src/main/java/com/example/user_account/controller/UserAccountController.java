package com.example.user_account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE // 이 부분은 그대로 둡니다.
    )
    public ResponseEntity<UserResponseDto> signup(
        @Valid @ModelAttribute UserSignupRequest request // @RequestPart("data") 대신 @ModelAttribute 사용
        // MultipartFile profileImage 파라미터는 제거합니다. DTO 내에 있으므로.
    ) {
        // request.setProfileImage(profileImage); // 이 라인은 이제 필요 없습니다.
                                            // @ModelAttribute가 자동으로 DTO의 profileImage 필드에 파일을 바인딩합니다.
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
      summary = "현재 사용자 프로필 조회",
      description = "JWT 토큰에서 추출한 현재 사용자의 프로필 정보를 조회합니다."
    )
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = UserResponseDto.class)
        )
      ),
      @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyProfile(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        String email = jwtUtil.extractEmail(token);
        UserResponseDto profile = userService.getUserProfileByEmail(email);
        return ResponseEntity.ok(profile);
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
    @RequestMapping(
      value = "/{id}",
      method = { RequestMethod.PUT, RequestMethod.POST },
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UserResponseDto> updateProfile(
      @PathVariable Long id,
      @Valid @ModelAttribute UserUpdateRequest request, // @RequestPart("data") 대신 @ModelAttribute 사용
      HttpServletRequest httpRequest // HttpServletRequest는 그대로 유지됩니다.
    ) {
        // 디버깅: 요청 데이터 로깅
        System.out.println("=== updateProfile 디버깅 ===");
        System.out.println("ID: " + id);
        System.out.println("Username: " + request.getUsername());
        System.out.println("Username is null: " + (request.getUsername() == null));
        System.out.println("Username isEmpty: " + (request.getUsername() != null && request.getUsername().isEmpty()));
        System.out.println("ProfileImage: " + (request.getProfileImage() != null ? request.getProfileImage().getOriginalFilename() : "null"));
        System.out.println("Content-Type: " + httpRequest.getContentType());
        
        // 모든 요청 파라미터 출력
        System.out.println("=== Request Parameters ===");
        httpRequest.getParameterMap().forEach((key, values) -> {
            System.out.println(key + ": " + java.util.Arrays.toString(values));
        });
        System.out.println("=========================");
        
        // JWT 검증 → 토큰에서 얻은 사용자 이메일과 경로변수 ID의 사용자 이메일 비교
        String token = extractTokenFromHeader(httpRequest);
        String email = jwtUtil.extractEmail(token);
        userService.verifyEmailMatchesId(email, id);

        UserResponseDto updated = userService.updateUser(id, request);
        return ResponseEntity.ok(updated);
    }
}
