package com.mango.mango.domain.cookItem.dto.response;

import com.mango.mango.domain.cookItem.entity.CookItem;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookItemResponseDto {

    private Long cookItemId;
    private String itemName;
    private int count;
    private String nutriUnit;
    private int nutriCapacity;
    private int nutriKcal;

    public static CookItemResponseDto fromEntity(CookItem cookItem) {
        return CookItemResponseDto.builder()
                .cookItemId(cookItem.getItemId())
                .itemName(cookItem.getItemName())
                .count(cookItem.getCount())
                .nutriUnit(cookItem.getNutriUnit())
                .nutriCapacity(cookItem.getNutriCapacity())
                .nutriKcal(cookItem.getNutriKcal())
                .build();
    }
}