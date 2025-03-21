package com.mango.mango.domain.cookItem.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.mango.mango.domain.cookItem.dto.request.CookItemRequestDto;
import com.mango.mango.domain.cookItem.dto.request.CookItemUpdateRequestDto;
import com.mango.mango.domain.cookItem.dto.response.CookItemDetailResponseDto;
import com.mango.mango.global.response.ApiResponse;

public interface CookItemService {
    ResponseEntity<ApiResponse<List<CookItemDetailResponseDto>>> getCookItemsByCookId(Long cookId);
    ResponseEntity<ApiResponse<Void>> addCookItem(Long cookId, CookItemRequestDto request);
    ResponseEntity<ApiResponse<Void>> updateCookItem(Long cookItemId, CookItemUpdateRequestDto request);
    ResponseEntity<ApiResponse<Void>> deleteCookItem(Long cookId);
}
