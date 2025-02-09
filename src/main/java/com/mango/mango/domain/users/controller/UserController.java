package com.mango.mango.domain.users.controller;

import com.mango.mango.domain.users.dto.request.UserSignUpRequestDto;
import com.mango.mango.domain.users.service.UserService;
import com.mango.mango.global.error.ErrorCode;
import com.mango.mango.global.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
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

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Long>> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        
        // 회원가입 처리
        try {
            Long userId = usersService.signUp(requestDto);
            return ResponseEntity.ok(ApiResponse.success(userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(ErrorCode.DUPLICATE_EMAIL.getCode(), e.getMessage()));
        }
    }

    //전화번호 중복 유효성 검사 존재: true, 존재하지 않음: false
    @PostMapping("/check-phone")
    public ResponseEntity<ApiResponse<Boolean>> checkPhone(@RequestParam String phoneNumber) {
        try{
            boolean isDuplicate = usersService.isPhoneDuplicate(phoneNumber);
            return ResponseEntity.ok(ApiResponse.success(isDuplicate));
        }catch(IllegalArgumentException e){
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                    ErrorCode.DUPLICATE_PHONE_NUMBER.getCode(),
                    ErrorCode.DUPLICATE_PHONE_NUMBER.getMessage()
                ));
        }
    }

    //닉네임 중복 유효성 검사
    @PostMapping("/check-nickname")
    public ResponseEntity<ApiResponse<Boolean>> checkNickname(@RequestParam String nickname) {
        try{
            boolean isDuplicate = usersService.isNicknameDuplicate(nickname);
            return ResponseEntity.ok(ApiResponse.success(isDuplicate));
        }catch(IllegalArgumentException e){
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                    ErrorCode.DUPLICATE_NICKNAME.getCode(),
                    ErrorCode.DUPLICATE_NICKNAME.getMessage()
                ));
        }
    }

    //이메일 중복 유효성 검사
    @PostMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam String email) {
        try{
            boolean isDuplicate = usersService.isEmailDuplicate(email);
            return ResponseEntity.ok(ApiResponse.success(isDuplicate));
        }catch(IllegalArgumentException e){
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                    ErrorCode.DUPLICATE_EMAIL.getCode(),
                    ErrorCode.DUPLICATE_EMAIL.getMessage()
                ));
        }
    }
} 