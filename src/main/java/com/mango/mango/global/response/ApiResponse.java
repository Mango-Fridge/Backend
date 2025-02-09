package com.mango.mango.global.response;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

/**
 * 글로벌 응답 형식 정의 클래스
 */
@Getter
@Builder
public class ApiResponse<T> {
    private Status status;
    private Integer code;    // 성공시
    private T data;         // 성공시
    private Error error;    // 실패시

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(Status.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder()
                .status(Status.FAILED)
                .error(Error.builder()
                        .code(code)
                        .message(message)
                        .build())
                .build();
    }

    @Getter
    @Builder
    public static class Error {
        private String code;
        private String message;
    }

    public enum Status {
        SUCCESS, FAILED
    }
} 