package com.mango.mango.domain.users.controller;

import com.mango.mango.domain.users.dto.request.UserSignUpRequestDto;
import com.mango.mango.domain.users.service.UserService;
import com.mango.mango.global.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService usersService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Long>> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        Long userId = usersService.signUp(requestDto);
        return ResponseEntity.ok(ApiResponse.success(userId));
    }

    // 전화번호 중복 유효성 검사 ( 존재: true, 존재하지 않음: false )
    @PostMapping("/check-phone")
    public ResponseEntity<ApiResponse<Boolean>> checkPhone(@RequestParam String phoneNumber) {
        boolean isDuplicate = usersService.isPhoneDuplicate(phoneNumber);
        return ResponseEntity.ok(ApiResponse.success(isDuplicate));
    }

    // 닉네임 중복 유효성 검사 ( 존재: true, 존재하지 않음: false )
    @PostMapping("/check-nickname")
    public ResponseEntity<ApiResponse<Boolean>> checkNickname(@RequestParam String nickname) {
        boolean isDuplicate = usersService.isNicknameDuplicate(nickname);
        return ResponseEntity.ok(ApiResponse.success(isDuplicate));
    }

    // 이메일 중복 유효성 검사 ( 존재: true, 존재하지 않음: false )
    @PostMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam String email) {
        boolean isDuplicate = usersService.isEmailDuplicate(email);
        return ResponseEntity.ok(ApiResponse.success(isDuplicate));
    }
}