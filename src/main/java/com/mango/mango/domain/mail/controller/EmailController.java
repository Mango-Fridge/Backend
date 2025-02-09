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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    // 이메일 인증 요청 API
    @PostMapping("/email/verify")
    public ResponseEntity<ApiResponse<String>> sendVerificationEmail(@RequestParam String email) {
        // 이메일 발송
        emailService.sendVerificationEmail(email);
        return ResponseEntity.ok(ApiResponse.success("인증번호가 발송되었습니다."));
    }

    // 인증번호 확인 API
    @PostMapping("/email/verify/confirm")
    public ResponseEntity<ApiResponse<Boolean>> verifyCode(@RequestBody EmailVerifyRequestDto requestDto) {
        // 이메일 인증
        boolean isValid = emailService.confirmCode(requestDto);
        return ResponseEntity.ok(ApiResponse.success(isValid));
    }
}
