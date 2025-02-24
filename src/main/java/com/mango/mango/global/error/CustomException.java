package com.mango.mango.global.error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final int remainingTime;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.remainingTime = 0;
    }

    public CustomException(ErrorCode errorCode, int remainingTime) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.remainingTime = remainingTime;
    }
}