package com.mango.mango.domain.item.service.impl;

import com.mango.mango.domain.contents.entity.Content;
import com.mango.mango.domain.contents.repository.ContentRepository;
import com.mango.mango.domain.groups.entity.Group;
import com.mango.mango.domain.groups.repository.GroupRepository;
import com.mango.mango.domain.item.dto.request.ItemRequestDto;
import com.mango.mango.domain.item.dto.response.ItemResponseDto;
import com.mango.mango.domain.item.dto.response.SearchItemResponseDto;
import com.mango.mango.domain.item.entity.Item;
import com.mango.mango.domain.item.repository.ItemRepository;
import com.mango.mango.domain.item.service.ItemService;
import com.mango.mango.global.error.CustomException;
import com.mango.mango.global.error.ErrorCode;
import com.mango.mango.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private GroupRepository groupRepository;


    // [3-1] 물품 추가 - 물품 추가 검색어 물품들 호출
    @Override
    public ResponseEntity<ApiResponse<SearchItemResponseDto>> searchItems(String keyword) {
        List<Item> items = itemRepository.findByItemNameContainingIgnoreCase(keyword);

        List<SearchItemResponseDto.searchItems> searchItemList = items.stream()
                .map(item -> new SearchItemResponseDto.searchItems(
                        item.getItemId(),
                        item.getItemName(),
                        item.getBrandName(),
                        item.getNutriUnit(),
                        item.getNutriCapacity(),
                        item.getNutriKcal()
                ))
                .toList();

        SearchItemResponseDto responseDto = new SearchItemResponseDto(searchItemList);

        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }


    // [3-1] 물품 추가 - 물품 추가 상세 페이지 데이터 호출
    @Override
    public ResponseEntity<ApiResponse<ItemResponseDto>> getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

        ItemResponseDto res = ItemResponseDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .category(item.getCategory())
                .subCategory(item.getSubCategory())
                .brandName(item.getBrandName())
                .nutriUnit(item.getNutriUnit())
                .nutriCapacity(item.getNutriCapacity())
                .nutriKcal(item.getNutriKcal())
                .nutriCarbohydrate(item.getNutriCarbohydrate())
                .nutriProtein(item.getNutriProtein())
                .nutrifat(item.getNutriFat())
                .build();

        return ResponseEntity.ok(ApiResponse.success(res));
    }


    // [3-1] 물품 추가 - 물품 추가
    @Transactional
    @Override
    public ResponseEntity<ApiResponse<?>> addItem(ItemRequestDto req) {
        Group group = groupRepository.findById(req.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        boolean isOpenItem = req.isOpenItem();

        // Item 저장
        if(isOpenItem){
            Item item = itemRepository.save(
                    Item.builder()
                            .itemName(req.getItemName())
                            .category(req.getCategory())
                            .subCategory(req.getSubCategory())
                            .brandName(req.getBrandName())
                            .storageArea(req.getStorageArea())
                            .nutriUnit(req.getNutriUnit())
                            .nutriCapacity(req.getNutriCapacity())
                            .nutriKcal(req.getNutriKcal())
                            .nutriCarbohydrate(req.getNutriCarbohydrate())
                            .nutriProtein(req.getNutriProtein())
                            .nutriFat(req.getNutriFat())
                            .build()
            );
        }

        // Content 저장
        Content content = contentRepository.save(
                Content.builder()
                        .contentName(req.getItemName())
                        .category(req.getCategory())
                        .subCategory(req.getSubCategory())
                        .brandName(req.getBrandName())
                        .count(req.getCount())
                        .regDate(req.getRegDate())
                        .expDate(req.getExpDate())
                        .storageArea(req.getStorageArea())
                        .memo(req.getMemo())
                        .nutriUnit(req.getNutriUnit())
                        .nutriCapacity(req.getNutriCapacity())
                        .nutriKcal(req.getNutriKcal())
                        .nutriCarbohydrate(req.getNutriCarbohydrate())
                        .nutriProtein(req.getNutriProtein())
                        .nutriFat(req.getNutriFat())
                        .group(group)
                        .build()
        );
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}