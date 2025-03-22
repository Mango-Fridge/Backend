package com.mango.mango.domain.cook.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.mango.mango.domain.cook.entity.Cook;
import com.mango.mango.domain.cookItem.dto.response.CookItemSummaryResponseDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookDetailResponseDto {
    private Long cookId;
    private String cookName;
    private String cookMemo;
    private int cookNutriKcal;
    private int cookNutriCarbohydrate;
    private int cookNutriProtein;
    private int cookNutriFat;
    private List<CookItemSummaryResponseDto> cookItems;

    public static CookDetailResponseDto fromEntity(Cook cook) {
        return CookDetailResponseDto.builder()
                .cookId(cook.getCookId())
                .cookName(cook.getCookName())
                .cookMemo(cook.getCookMemo())
                .cookNutriKcal(cook.getCookNutriKcal())
                .cookNutriCarbohydrate(cook.getCookNutriCarbohydrate())
                .cookNutriProtein(cook.getCookNutriProtein())
                .cookNutriFat(cook.getCookNutriFat())
                .cookItems(cook.getCookItems().stream()
                        .map(CookItemSummaryResponseDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    } 
}
