package com.mango.mango.domain.cook.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mango.mango.domain.cook.dto.request.CookRequestDto;
import com.mango.mango.domain.cook.dto.request.CookRequestDto2;
import com.mango.mango.domain.cook.dto.request.CookUpdateRequestDto;
import com.mango.mango.domain.cook.dto.response.CookDetailResponseDto;
import com.mango.mango.domain.cook.dto.response.CookSummaryResponseDto;
import com.mango.mango.domain.cook.entity.Cook;
import com.mango.mango.domain.cook.repository.CookRepository;
import com.mango.mango.domain.cook.service.CookService;
import com.mango.mango.domain.cookItem.entity.CookItem;
import com.mango.mango.domain.cookItem.repository.CookItemRepository;
import com.mango.mango.domain.groups.entity.Group;
import com.mango.mango.domain.groups.repository.GroupRepository;
import com.mango.mango.global.error.CustomException;
import com.mango.mango.global.error.ErrorCode;
import com.mango.mango.global.response.ApiResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CookServiceImpl implements CookService{

    private final GroupRepository groupRepository;
    private final CookRepository cookRepository;
    private final CookItemRepository cookItemRepository;

    @Override
    public ResponseEntity<ApiResponse<Void>> addNewCook(CookRequestDto request) {
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
        
        Cook cook = Cook.builder()
        .cookName(request.getCookName())
        .cookMemo(request.getCookMemo())
        .cookNutriKcal(request.getCookNutriKcal())
        .cookNutriCarbohydrate(request.getCookNutriCarbohydrate())
        .cookNutriProtein(request.getCookNutriProtein())
        .cookNutriFat(request.getCookNutriFat())
        .group(group)
        .build();

        cookRepository.save(cook);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Void>> addNewCook2(CookRequestDto2 request) {
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
    
        Cook cook = Cook.builder()
                .cookName(request.getCookName())
                .cookMemo(request.getCookMemo())
                .cookNutriKcal(request.getCookNutriKcal())
                .cookNutriCarbohydrate(request.getCookNutriCarbohydrate())
                .cookNutriProtein(request.getCookNutriProtein())
                .cookNutriFat(request.getCookNutriFat())
                .group(group)
                .build();
    
        cookRepository.save(cook);
    
        if (request.getCookItems() != null && !request.getCookItems().isEmpty()) {
            List<CookItem> cookItems = request.getCookItems().stream()
                .map(itemDto -> CookItem.builder()
                        .itemName(itemDto.getItemName())
                        .count(itemDto.getCount())
                        .nutriUnit(itemDto.getNutriUnit())
                        .category(itemDto.getCategory())
                        .brandName(itemDto.getBrandName())
                        .storageArea(itemDto.getStorageArea())
                        .nutriCapacity(itemDto.getNutriCapacity())
                        .nutriKcal(itemDto.getNutriKcal())
                        .cook(cook)
                        .build())
            .toList();
    
            cookItemRepository.saveAll(cookItems);
        }
    
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    
    @Override
    public ResponseEntity<ApiResponse<List<CookSummaryResponseDto>>> getCookList(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        List<Cook> cooks = cookRepository.findByGroup(group);

        List<CookSummaryResponseDto> cookSummaries = cooks.stream()
                .map(CookSummaryResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(cookSummaries));
    }

    @Override
    public ResponseEntity<ApiResponse<CookDetailResponseDto>> getCookDetail(Long cookId) {
        Cook cook = cookRepository.findById(cookId)
            .orElseThrow(() -> new CustomException(ErrorCode.COOK_NOT_FOUND));

        CookDetailResponseDto responseDto = CookDetailResponseDto.fromEntity(cook);

        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> updateCookDetail(Long cookId, CookUpdateRequestDto request){
        Cook cook = cookRepository.findById(cookId)
            .orElseThrow(() -> new CustomException(ErrorCode.COOK_NOT_FOUND));
        
        cook.setCookName(request.getCookName());
        cook.setCookMemo(request.getCookMemo());
        cook.setCookNutriKcal(request.getCookNutriKcal());
        cook.setCookNutriCarbohydrate(request.getCookNutriCarbohydrate());
        cook.setCookNutriProtein(request.getCookNutriProtein());
        cook.setCookNutriFat(request.getCookNutriFat());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteCook(Long cookId){
        Cook cook = cookRepository.findById(cookId)
            .orElseThrow(() -> new CustomException(ErrorCode.COOK_NOT_FOUND));
        
        cookItemRepository.deleteAllByCook(cook);

        cookRepository.delete(cook);

        return ResponseEntity.ok(ApiResponse.success(null));
    }


}
