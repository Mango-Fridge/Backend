package com.mango.mango.domain.item.service.impl;

import com.mango.mango.domain.contents.entity.Content;
import com.mango.mango.domain.contents.repository.ContentRepository;
import com.mango.mango.domain.groups.entity.Group;
import com.mango.mango.domain.groups.repository.GroupRepository;
import com.mango.mango.domain.item.dto.request.ItemRequestDto;
import com.mango.mango.domain.item.dto.response.ItemResponseDto;
import com.mango.mango.domain.item.entity.Item;
import com.mango.mango.domain.item.repository.ItemRepository;
import com.mango.mango.domain.item.service.ItemService;
import com.mango.mango.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    // 물품 추가 검색어 물품들 호출
    @Override
    public ResponseEntity<ApiResponse<List<Item>>> searchItems(String keyword) {
        List<Item> items = itemRepository.findByItemNameContainingIgnoreCase(keyword);
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    // 물품 추가 상세 페이지 데이터 호출
    @Override
    public ResponseEntity<ApiResponse<ItemResponseDto>> getItemById(Long itemId) {
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if(itemOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.error(
                                    "404",
                                    "존재하지 않는 Item ID: " + itemId
                            )
                    );
        }

        Item item = itemOpt.get();
        ItemResponseDto res = ItemResponseDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .category(item.getCategory())
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

    // 물품 추가
    @Override
    public ResponseEntity<ApiResponse<?>> addItem(ItemRequestDto req) {
        Optional<Group> groupOpt = groupRepository.findById(req.getGroupId());
        if(groupOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.error(
                                    "404",
                                    "존재하지 않는 Group ID: " + req.getGroupId()
                            )
                    );
        }
        Group group = groupOpt.get();

        boolean isOpenItem = req.isOpenItem();

        // Item 저장
        if(isOpenItem){
            Item item = itemRepository.save(
                    Item.builder()
                            .itemName(req.getItemName())
                            .category(req.getCategory())
                            .brandName(req.getBrandName())
                            .storageArea(req.getStorageArea())
                            .nutriUnit(req.getNutriUnit())
                            .nutriCapacity(req.getNutriCapacity())
                            .nutriKcal(req.getNutriKcal())
                            .nutriCarbohydrate(req.getNutriCarbohydrate())
                            .nutriProtein(req.getNutriProtein())
                            .nutriFat(req.getNutrifat())
                            .build()
            );
        }

        // Content 저장
        Content content = contentRepository.save(
                Content.builder()
                        .contentName(req.getItemName())
                        .category(req.getCategory())
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
                        .nutriFat(req.getNutrifat())
                        .group(group)
                        .build()
        );
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}