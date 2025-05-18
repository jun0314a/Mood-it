package com.example.user_account.controller;

import com.example.user_account.entity.User;
import com.example.user_account.repository.UserRepository;
import com.example.user_account.service.EmailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class PasswordResetController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "비밀번호 찾기", description = "이메일로 임시 비밀번호를 발송합니다.")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "임시 비밀번호 발송"),
      @ApiResponse(responseCode = "400", description = "등록된 사용자가 없음")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("해당 이메일로 등록된 사용자가 없습니다.");
        }

        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        String encoded = passwordEncoder.encode(tempPassword);

        User user = optionalUser.get();
        user.setPassword(encoded);
        userRepository.save(user);

        emailService.sendVerificationCode(email, "임시 비밀번호: " + tempPassword);
        return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");
    }

    @Operation(summary = "비밀번호 재설정", description = "이메일과 새 비밀번호로 등록된 회원의 비밀번호를 변경합니다.")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
      @ApiResponse(responseCode = "400", description = "존재하지 않는 이메일")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("존재하지 않는 이메일입니다.");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}

