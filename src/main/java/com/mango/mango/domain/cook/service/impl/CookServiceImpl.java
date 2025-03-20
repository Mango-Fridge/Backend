package com.mango.mango.domain.cook.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mango.mango.domain.cook.dto.request.CookRequestDto;
import com.mango.mango.domain.cook.dto.response.CookSummaryResponseDto;
import com.mango.mango.domain.cook.entity.Cook;
import com.mango.mango.domain.cook.repository.CookRepository;
import com.mango.mango.domain.cook.service.CookService;
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

    private GroupRepository groupRepository;
    private CookRepository cookRepository;

    @Override
    public ResponseEntity<ApiResponse<Void>> addNewCook(CookRequestDto cookRequestDto) {
        Group group = groupRepository.findById(cookRequestDto.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
        
                Cook cook = Cook.builder()
                .cookName(cookRequestDto.getCookName())
                .cookMemo(cookRequestDto.getCookMemo())
                .cookNutriKcal(cookRequestDto.getCookNutriKcal())
                .cookNutriCarbohydrate(cookRequestDto.getCookNutriCarbohydrate())
                .cookNutriProtein(cookRequestDto.getCookNutriProtein())
                .cookNutriFat(cookRequestDto.getCookNutriFat())
                .group(group)
                .build();

        cookRepository.save(cook);

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

}
