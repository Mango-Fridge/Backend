package com.mango.mango.domain.cookItem.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mango.mango.domain.cook.entity.Cook;
import com.mango.mango.domain.cook.repository.CookRepository;
import com.mango.mango.domain.cookItem.dto.request.CookItemRequestDto;
import com.mango.mango.domain.cookItem.dto.request.CookItemUpdateRequestDto;
import com.mango.mango.domain.cookItem.dto.response.CookItemDetailResponseDto;
import com.mango.mango.domain.cookItem.entity.CookItem;
import com.mango.mango.domain.cookItem.repository.CookItemRepository;
import com.mango.mango.domain.cookItem.service.CookItemService;
import com.mango.mango.global.error.CustomException;
import com.mango.mango.global.error.ErrorCode;
import com.mango.mango.global.response.ApiResponse;

@Service
public class CookItemServiceImpl implements CookItemService{

    @Autowired
    private CookItemRepository cookItemRepository;

    @Autowired
    private CookRepository cookRepository;

    @Override
    @SuppressWarnings("unused")
    public ResponseEntity<ApiResponse<List<CookItemDetailResponseDto>>> getCookItemsByCookId(Long cookId) {
        Cook cook = cookRepository.findById(cookId)
            .orElseThrow(() -> new CustomException(ErrorCode.COOK_NOT_FOUND));
    
        List<CookItem> cookItems = cookItemRepository.findByCook_CookId(cookId);
    
        List<CookItemDetailResponseDto> response = cookItems.stream()
            .map(CookItemDetailResponseDto::fromEntity)
            .collect(Collectors.toList());
    
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> addCookItem(Long cookId, CookItemRequestDto requestDto) {
        Cook cook = cookRepository.findById(cookId)
            .orElseThrow(() -> new CustomException(ErrorCode.COOK_NOT_FOUND));

        CookItem cookItem = CookItem.builder()
            .itemName(requestDto.getItemName())
            .count(requestDto.getCount())
            .category(requestDto.getCategory())
            .brandName(requestDto.getBrandName())
            .storageArea(requestDto.getStorageArea())
            .nutriUnit(requestDto.getNutriUnit())
            .nutriCapacity(requestDto.getNutriCapacity())
            .nutriKcal(requestDto.getNutriKcal())
            .cook(cook)
            .build();

        cookItemRepository.save(cookItem);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> updateCookItem(Long cookItemId, CookItemUpdateRequestDto request) {
        CookItem cookItem = cookItemRepository.findById(cookItemId)
            .orElseThrow(() -> new CustomException(ErrorCode.COOK_ITEM_NOT_FOUND));

        cookItem.setItemName(request.getItemName());
        cookItem.setCount(request.getCount());
        cookItem.setCategory(request.getCategory());
        cookItem.setBrandName(request.getBrandName());
        cookItem.setStorageArea(request.getStorageArea());
        cookItem.setNutriUnit(request.getNutriUnit());
        cookItem.setNutriCapacity(request.getNutriCapacity());
        cookItem.setNutriKcal(request.getNutriKcal());

        cookItemRepository.save(cookItem);
        
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteCookItem(Long cookItemId) {
        CookItem cookItem = cookItemRepository.findById(cookItemId)
            .orElseThrow(() -> new CustomException(ErrorCode.COOK_ITEM_NOT_FOUND));
        cookItemRepository.delete(cookItem);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
