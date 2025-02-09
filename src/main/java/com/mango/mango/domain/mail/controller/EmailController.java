package com.mango.mango.domain.mail.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import com.mango.mango.domain.mail.service.EmailService;
import com.mango.mango.domain.mail.dto.request.EmailVerifyRequestDto;
import com.mango.mango.global.response.ApiResponse;

import java.time.Duration;
import java.util.Random;

import org.springframework.data.redis.core.RedisTemplate;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class EmailController {
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailService emailService;
    // 이메일 인증 요청 API
    @PostMapping("/email/verify")
    public ResponseEntity<ApiResponse<String>> sendVerificationEmail(@RequestParam String email) {
        // 인증번호 생성 (6자리)
        String verificationCode = generateVerificationCode();
        
        // Redis에 인증번호 저장 (만료시간 5분)
        redisTemplate.opsForValue()
            .set("EmailVerification:" + email, verificationCode, 
                 Duration.ofMinutes(5));
        
        // 이메일 발송
        emailService.sendVerificationEmail(email, verificationCode);
        
        return ResponseEntity.ok(ApiResponse.success("인증번호가 발송되었습니다."));
    }

    // 인증번호 확인 API
    @PostMapping("/email/verify/confirm")
    public ResponseEntity<ApiResponse<Boolean>> verifyCode(@RequestBody EmailVerifyRequestDto requestDto) {
        String savedCode = redisTemplate.opsForValue()
            .get("EmailVerification:" + requestDto.getEmail());
        
        boolean isValid = requestDto.getCode().equals(savedCode);
        
        if (isValid) {
            redisTemplate.delete("EmailVerification:" + requestDto.getEmail());
        }
        
        return ResponseEntity.ok(ApiResponse.success(isValid));
    }

    private String generateVerificationCode() {
        // 6자리 랜덤 숫자 생성
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        
        return code.toString();
    }
}
