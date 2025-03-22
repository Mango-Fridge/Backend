package com.mango.mango.domain.cookItem.dto.response;

import com.mango.mango.domain.cookItem.entity.CookItem;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookItemDetailResponseDto {
    private Long itemId;
    private String itemName;
    private int count;
    private String category;
    private String brandName;
    private String storageArea;
    private String nutriUnit;
    private int nutriCapacity;
    private int nutriKcal;

    public static CookItemDetailResponseDto fromEntity(CookItem item) {
        return CookItemDetailResponseDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .count(item.getCount())
                .category(item.getCategory())
                .brandName(item.getBrandName())
                .storageArea(item.getStorageArea())
                .nutriUnit(item.getNutriUnit())
                .nutriCapacity(item.getNutriCapacity())
                .nutriKcal(item.getNutriKcal())
                .build();
    }
}