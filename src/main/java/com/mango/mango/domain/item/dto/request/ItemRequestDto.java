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
    private String NutriUnit;
    private int NutriCapacity;
    private int NutriKcal;
    private int NutriCarbohydrate;
    private int NutriProtein;
    private int Nutrifat;
}