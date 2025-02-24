package com.mango.mango.global.error;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;

import com.mango.mango.global.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException e) {
        return ResponseEntity
            .badRequest()
            .body(ApiResponse.error(e.getErrorCode().getCode(), e.getErrorCode().getMessage()));
    }

}
