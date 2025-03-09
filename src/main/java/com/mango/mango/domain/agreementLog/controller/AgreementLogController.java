package com.mango.mango.domain.agreementLog.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mango.mango.domain.agreementLog.dto.request.AgreementRequestDto;
import com.mango.mango.domain.agreementLog.dto.response.AgreementResponseDto;
import com.mango.mango.domain.agreementLog.service.AgreementLogService;
import com.mango.mango.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Agreement", description = "약관 동의 관련 API")
@RestController
@RequestMapping("/agreement")
@RequiredArgsConstructor
public class AgreementLogController {

    private final AgreementLogService agreementLogService;
    
    @PostMapping("/agree")
    public ResponseEntity<ApiResponse<AgreementResponseDto>> login(@Valid @RequestBody AgreementRequestDto requestDto){
        AgreementResponseDto response = agreementLogService.agree(requestDto);
    
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
