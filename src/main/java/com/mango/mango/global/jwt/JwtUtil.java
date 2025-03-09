package com.mango.mango.global.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24시간
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final Key secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * JWT 토큰 생성
     */
    @SuppressWarnings("deprecation")
    public String createToken(Long email) {
        return Jwts.builder()
            .setSubject(String.valueOf(email)) // email subject로 설정
            .setIssuedAt(new Date()) // 발급 시간
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
            .signWith(secretKey, SignatureAlgorithm.HS256) // 비밀 키 서명
            .compact();
    }

    /**
     * JWT 토큰 검증 및 파싱
     */
    @SuppressWarnings("deprecation")
    public Long validateAndGetUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

            return Long.parseLong(claims.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("유효하지 않은 JWT 토큰입니다.");
        }
    }
}