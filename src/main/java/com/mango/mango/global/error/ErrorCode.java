package com.mango.mango.global.error;

import lombok.Getter;


/**
 * 전역 에러 코드 정의 Enum
 * return
 */
@Getter
public enum ErrorCode {

    // Common
//    INVALID_INPUT_VALUE("C001", "Invalid Input Value"),
//    INTERNAL_SERVER_ERROR("C002", "Internal Server Error"),
    
    // User
    INVALID_EMAIL_FORMAT("U001", "올바른 이메일 형식이 아닙니다."),
    DUPLICATE_EMAIL("U002", "이미 가입된 이메일입니다."),
    INVALID_PASSWORD("U003", "비밀번호 형식이 올바르지 않습니다."),
    REQUIRED_AGREEMENT("U004", "필수 약관에 동의해주세요."),
    DUPLICATE_PHONE_NUMBER("U005", "이미 가입된 전화번호 입니다."),
    DUPLICATE_NICKNAME("U006", "이미 가입된 닉네임 입니다."),
    USER_NOT_FOUND("U007", "해당 유저를 찾을 수 없습니다."),
    
    // OAuth
    INVALID_OAUTH_TOKEN("O001", "유효하지 않은 토큰입니다."),
    INVALID_OAUTH_PROVIDER("O002", "지원되지 않는 OAuth 제공자입니다."),

    // Content
    CONTENT_NOT_FOUND("C001", "해당 콘텐츠을 찾을 수 없습니다."),
    INVALID_ITEM_COUNT("C002", "품목 개수가 0보다 작을 수 없습니다."),

    // Item
    ITEM_NOT_FOUND("I001", "해당 아이템을 찾을 수 없습니다."),

    // Cook
    COOK_NOT_FOUND("CO001", "해당 요리를 찾을 수 없습니다."),

    // CookItem
    COOK_ITEM_NOT_FOUND("CI001", "해당 재료를 찾을 수 없습니다."),

    // Group
    GROUP_NOT_FOUND("G001", "해당 그룹을 찾을 수 없습니다.");


    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
} 