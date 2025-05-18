package com.example.user_account.controller;

import com.example.user_account.repository.UserRepository;
import com.example.user_account.service.EmailService;
import com.example.user_account.service.VerificationCodeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Random;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailService emailService;
    private final VerificationCodeService codeService;
    private final UserRepository userRepository;

    @Operation(summary = "인증 코드 전송", description = "이메일로 6자리 인증 코드를 전송합니다.")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "코드 전송 완료")
    })
    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestParam String email) {
        String code = String.valueOf(new Random().nextInt(900000) + 100000); // 6자리 숫자
        emailService.sendVerificationCode(email, code);
        codeService.createVerificationCode(email, code);
        return ResponseEntity.ok("인증 코드가 전송되었습니다.");
    }

    @Operation(summary = "이메일 인증", description = "이메일/코드를 검증하여 회원의 verified 상태를 true로 변경합니다.")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "인증 성공"),
      @ApiResponse(responseCode = "400", description = "인증 실패(코드 불일치)")
    })
    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String email, @RequestParam String code) {
        if (!codeService.verifyCode(email, code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 실패: 코드 불일치");
        }

        userRepository.findByEmail(email).ifPresent(user -> {
            user.setVerified(true);
            userRepository.save(user);
        });

        return ResponseEntity.ok("이메일 인증 성공!");
    }
}
