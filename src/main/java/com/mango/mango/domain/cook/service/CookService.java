package com.mango.mango.domain.cook.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.mango.mango.domain.cook.dto.request.CookRequestDto;
import com.mango.mango.domain.cook.dto.request.CookUpdateRequestDto;
import com.mango.mango.domain.cook.dto.response.CookDetailResponseDto;
import com.mango.mango.domain.cook.dto.response.CookSummaryResponseDto;
import com.mango.mango.global.response.ApiResponse;

public interface CookService {
    ResponseEntity<ApiResponse<Void>> addNewCook(CookRequestDto request) throws Exception;
    ResponseEntity<ApiResponse<List<CookSummaryResponseDto>>> getCookList(Long groupId) throws Exception;
    ResponseEntity<ApiResponse<CookDetailResponseDto>> getCookDetail(Long cookId) throws Exception;
    ResponseEntity<ApiResponse<Void>> updateCookDetail(Long cookId, CookUpdateRequestDto request) throws Exception;
    ResponseEntity<ApiResponse<Void>> deleteCook(Long cookId) throws Exception;
}
