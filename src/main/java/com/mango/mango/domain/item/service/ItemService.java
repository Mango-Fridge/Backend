package com.mango.mango.domain.item.service;

import com.mango.mango.domain.item.dto.request.ItemRequestDto;
import com.mango.mango.domain.item.dto.response.ItemResponseDto;
import com.mango.mango.domain.item.dto.response.SearchItemResponseDto;
import com.mango.mango.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ItemService {
    ResponseEntity<ApiResponse<SearchItemResponseDto>> searchItems(String keyword);
    ResponseEntity<ApiResponse<ItemResponseDto>> getItemById(Long itemId);
    ResponseEntity<ApiResponse<?>> addItem(ItemRequestDto req);
}