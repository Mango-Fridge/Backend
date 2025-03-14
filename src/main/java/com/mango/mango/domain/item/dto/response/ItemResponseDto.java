package com.mango.mango.domain.item.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemResponseDto {
    private Long itemId;
    private String itemName;
    private String category;
    private String brandName;
    private String NutriUnit;
    private int NutriCapacity;
    private int NutriKcal;
    private int NutriCarbohydrate;
    private int NutriProtein;
    private int Nutrifat;

    private List<searchItems> items;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class searchItems {
        private Long itemId;
        private String itemName;
        private String brandName;
        private String nutriUnit;
        private int nutriCapacity;
        private int nutriKcal;
    }

    @Builder
    public ItemResponseDto(
            Long itemId,
            String itemName,
            String category,
            String brandName,
            String nutriUnit,
            int nutriCapacity,
            int nutriKcal,
            int nutriCarbohydrate,
            int nutriProtein,
            int nutrifat
    ) {
        this.itemId = itemId;
        this.category = category;
        this.itemName = itemName;
        this.brandName = brandName;
        this.NutriUnit = nutriUnit;
        this.NutriCapacity = nutriCapacity;
        this.NutriKcal = nutriKcal;
        this.NutriCarbohydrate = nutriCarbohydrate;
        this.NutriProtein = nutriProtein;
        this.Nutrifat = nutrifat;
    }
}