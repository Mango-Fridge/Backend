package com.mango.mango.domain.cook.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.mango.mango.domain.cook.dto.request.CookRequestDto;
import com.mango.mango.domain.cook.dto.response.CookSummaryResponseDto;
import com.mango.mango.global.response.ApiResponse;

public interface CookService {
    ResponseEntity<ApiResponse<Void>> addNewCook(CookRequestDto cookRequestDto) throws Exception;
    ResponseEntity<ApiResponse<List<CookSummaryResponseDto>>> getCookList(Long groupId) throws Exception;
}
