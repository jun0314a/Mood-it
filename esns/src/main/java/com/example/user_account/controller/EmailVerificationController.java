package com.example.user_account.controller;

import com.example.user_account.repository.UserRepository;
import com.example.user_account.service.EmailService;
import com.example.user_account.service.VerificationCodeService;
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

    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestParam String email) {
        String code = String.valueOf(new Random().nextInt(900000) + 100000); // 6자리 숫자
        emailService.sendVerificationCode(email, code);
        codeService.createVerificationCode(email, code);
        return ResponseEntity.ok("인증 코드가 전송되었습니다.");
    }

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
