package com.mango.mango.domain.cookItem.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookItemUpdateRequestDto {
    private String itemName;
    private int count;
    private String category;
    private String brandName;
    private String storageArea;
    private String nutriUnit;
    private int nutriCapacity;
    private int nutriKcal;
}