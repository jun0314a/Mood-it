package com.example.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.auth.dto.LoginRequest;
import com.example.auth.jwt.JwtUtil;
import com.example.user_account.entity.User;
import com.example.user_account.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Operation(summary = "로그인", description = "이메일/비밀번호로 인증 후 JWT 토큰을 발급합니다.")
    @ApiResponses({
      @ApiResponse(
        responseCode="200",
        description="로그인 성공",
        content=@Content(
          mediaType="application/json",
          schema=@Schema(example="{\"token\":\"eyJ...\",\"email\":\"user@ex.com\"}")
        )
      ),
      @ApiResponse(responseCode="401", description="인증 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // 🔐 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 👤 사용자 정보 조회
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 🧾 JWT Claim 세팅 (원하면 userId, role 등 추가 가능)
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", "USER"); // or user.getRole() if you have one
            claims.put("userId", user.getId());
            claims.put("email", user.getEmail());

            // 🪙 토큰 생성
            String token = jwtUtil.generateToken(user.getEmail(), claims);

            // ✅ 응답 형태 구성 (token + username 등)
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", user.getEmail());

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
        }
    }
}
