package com.example.user_account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private static final String PREFIX = "email:verify:";
    private static final Duration EXPIRE_TIME = Duration.ofMinutes(5); // 만료 시간

    private final StringRedisTemplate redisTemplate;

    // 인증 코드 저장
    public void createVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(PREFIX + email, code, EXPIRE_TIME);
    }

// 인증 코드 검증
    public boolean verifyCode(String email, String inputCode) {
        String key = PREFIX + email;
        String savedCode = redisTemplate.opsForValue().get(key);
    
        if (savedCode != null && savedCode.equals(inputCode)) {
            // ✅ 인증 완료 상태를 따로 저장 (5~10분 유효기간 줘도 되고 안 줘도 됨)
            redisTemplate.opsForValue().set("email:verified:" + email, "true", Duration.ofMinutes(10));
            return true;
        }

        return false;
    }

    public boolean isEmailVerified(String email) {
        String result = redisTemplate.opsForValue().get("email:verified:" + email);
        return "true".equals(result);
    }
}