package com.mango.mango.domain.item.service;

import com.mango.mango.domain.item.dto.request.ItemRequestDto;
import com.mango.mango.domain.item.dto.response.ItemResponseDto;
import com.mango.mango.domain.item.entity.Item;
import com.mango.mango.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ItemService {
    ResponseEntity<ApiResponse<List<Item>>> searchItems(String keyword);
    ResponseEntity<ApiResponse<ItemResponseDto>> getItemById(Long itemId);
    ResponseEntity<ApiResponse<?>> addItem(ItemRequestDto req);
}