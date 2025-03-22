package com.mango.mango.domain.cookItem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mango.mango.domain.cookItem.dto.request.CookItemRequestDto;
import com.mango.mango.domain.cookItem.dto.request.CookItemUpdateRequestDto;
import com.mango.mango.domain.cookItem.dto.response.CookItemDetailResponseDto;
import com.mango.mango.domain.cookItem.service.CookItemService;
import com.mango.mango.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cook-items")
@Tag(name = "CookItem", description = "요리 재료 관련 API")
public class CookItemController {

    @Autowired
    private CookItemService cookItemService;

    /**
     * 요리 재료 조회
     * @param cookId
     * @return
     */
    @GetMapping("/{cookId}")
    @Operation(
        summary = "요리 재료 - 재료 리스트 상세 정보",
        description = "특정 요리의 모든 재료의 상세정보를 전부 조회한다.")
    public ResponseEntity<ApiResponse<List<CookItemDetailResponseDto>>> getCookItemList(@PathVariable Long cookId) {
        return cookItemService.getCookItemsByCookId(cookId);
    }

    /**
     * 요리 재료 추가
     * @param cookId
     * @param request
     * @return
     */
    @PostMapping("/{cookId}")
    @Operation(
        summary = "요리 재료 - 재료 추가",
        description = "요리에 새로운 재료를 추가한다.")
    public ResponseEntity<ApiResponse<Void>> addCookItem(@PathVariable Long cookId, @RequestBody @Valid CookItemRequestDto request) {
        return cookItemService.addCookItem(cookId, request);
    }

    /**
     * 요리 재료 정보 수정
     * @param cookItemId
     * @param request
     * @return
     */
    @PutMapping("/{cookItemId}")
    @Operation(
        summary = "요리 재료 - 재료 정보 수정",
        description = "기존 요리 재료 정보를 수정한다.")
    public ResponseEntity<ApiResponse<Void>> updateCookItem(@PathVariable Long cookItemId, @RequestBody @Valid CookItemUpdateRequestDto request) {
        return cookItemService.updateCookItem(cookItemId, request);
    }

    /**
     * 요리 재료 삭제
     * @param cookItemId
     * @return
     */
    @DeleteMapping("/{cookItemId}")
    @Operation(
        summary = "요리 재료 - 재료 삭제",
        description = "요리 재료를 삭제한다.")
    public ResponseEntity<ApiResponse<Void>> deleteCookItem(@PathVariable Long cookItemId) {
        return cookItemService.deleteCookItem(cookItemId);
    }
}