package com.mango.mango.domain.item.controller;

import com.mango.mango.domain.item.dto.request.ItemRequestDto;
import com.mango.mango.domain.item.dto.response.ItemResponseDto;
import com.mango.mango.domain.item.dto.response.SearchItemResponseDto;
import com.mango.mango.domain.item.service.ItemService;
import com.mango.mango.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Tag(name = "Item", description = "공통 물품 관련 API")
public class ItemController {
    private final ItemService itemService;

    @Operation(summary = "[3-1] 물품 추가 - 물품 추가 검색어 물품들 호출")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<SearchItemResponseDto>> searchItems(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page) {
        return itemService.searchItems(keyword, page);
    }

    @Operation(summary = "[3-1] 물품 추가 - 물품 추가 상세 페이지 데이터 호출")
    @GetMapping("/{itemId}")
    public ResponseEntity<ApiResponse<ItemResponseDto>> getItemDetail(@PathVariable Long itemId){
        return itemService.getItemById(itemId);
    }

    @Operation(summary = "[3-1] 물품 추가 - 물품 추가")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> addItem(@RequestBody ItemRequestDto req) {
        return itemService.addItem(req);
    }
}