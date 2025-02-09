package com.mango.mango.global.error;

import lombok.Getter;


/**
 * 전역 에러 코드 정의 Enum
 */
@Getter
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE("C001", "Invalid Input Value"),
    INTERNAL_SERVER_ERROR("C002", "Internal Server Error"),
    
    // User
    INVALID_EMAIL_FORMAT("U001", "올바른 이메일 형식이 아닙니다."),
    DUPLICATE_EMAIL("U002", "이미 가입된 이메일입니다."),
    INVALID_PASSWORD("U003", "비밀번호 형식이 올바르지 않습니다."),
    REQUIRED_AGREEMENT("U004", "필수 약관에 동의해주세요."),
    DUPLICATE_PHONE_NUMBER("U005", "이미 가입된 전화번호 입니다."),
    DUPLICATE_NICKNAME("U006", "이미 가입된 닉네임 입니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
} 