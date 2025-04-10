package com.example.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.auth.dto.LoginRequest;
import com.example.auth.jwt.JwtUtil;
import com.example.user_account.entity.User;
import com.example.user_account.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

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

            // 👤 사용자 정보 조회
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 🧾 JWT Claim 세팅 (원하면 userId, role 등 추가 가능)
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", "USER"); // or user.getRole() if you have one
            claims.put("userId", user.getId());

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
