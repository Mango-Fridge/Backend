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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService usersService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Long>> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";

        // 회원가입 요청 Validation 체크
        if (!requestDto.getEmail().matches(emailRegex)) { // 이메일 형식 체크
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                    ErrorCode.INVALID_EMAIL_FORMAT.getCode(),
                    ErrorCode.INVALID_EMAIL_FORMAT.getMessage()
                ));
        }else if(!requestDto.getPrivacyAgreement().equals(false) ||
                !requestDto.getServiceAgreement().equals(false)){ // 필수 약관 동의 체크
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                    ErrorCode.REQUIRED_AGREEMENT.getCode(),
                    ErrorCode.REQUIRED_AGREEMENT.getMessage()
                ));
        }
        
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
} 