package com.mango.mango.domain.item.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchItemResponseDto {
    private List<searchItems> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class searchItems {
        private Long itemId;
        private String itemName;
        private String brandName;
        private String nutriUnit;
        private int nutriCapacity;
        private int nutriKcal;
    }
}