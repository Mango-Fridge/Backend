package com.mango.mango.domain.item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private boolean isOpenItem;
    private Long groupId;
    private String itemName;
    private String category;
    private String brandName;
    private int count;
    private LocalDateTime regDate;
    private LocalDateTime expDate;
    private String storageArea;
    private String memo;
    private String nutriUnit;
    private int nutriCapacity;
    private int nutriKcal;
    private int nutriCarbohydrate;
    private int nutriProtein;
    private int nutriFat;

    public ItemRequestDto(
            Long groupId,
            String itemName,
            String category,
            String brandName,
            String storageArea,
            String nutriUnit,
            int nutriCapacity,
            int nutriKcal,
            int nutriCarbohydrate,
            int nutriProtein,
            int nutriFat,
            boolean isOpenItem) {
        this.groupId = groupId;
        this.itemName = itemName;
        this.category = category;
        this.brandName = brandName;
        this.storageArea = storageArea;
        this.nutriUnit = nutriUnit;
        this.nutriCapacity = nutriCapacity;
        this.nutriKcal = nutriKcal;
        this.nutriCarbohydrate = nutriCarbohydrate;
        this.nutriProtein = nutriProtein;
        this.nutriFat = nutriFat;
        this.isOpenItem = isOpenItem;
    }
}