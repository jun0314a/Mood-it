package com.example.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyPlain;

    private Key signingKey;

    // ⏰ 만료 시간: 1일
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(secretKeyPlain.getBytes());
    }

    // ✅ 토큰 생성 (username + custom claims 추가 가능)
    public String generateToken(String username, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setSubject(username)
                .addClaims(extraClaims) // 추가 정보 (ex: role, userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ 기본 생성자 (username만)
    public String generateToken(String username) {
        return generateToken(username, Map.of());
    }

    // ✅ 사용자명 추출
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // ✅ 커스텀 클레임 전체 가져오기
    public Claims extractAllClaims(String token) {
        return parseClaims(token);
    }

    // ✅ 유효성 검증
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid JWT: " + e.getMessage());
            return false;
        }
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Integer.class).longValue(); // or Long.class
    }
    

    // ✅ 내부용: 토큰 파싱 메서드
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
