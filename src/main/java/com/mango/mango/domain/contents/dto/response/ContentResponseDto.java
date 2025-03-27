package com.mango.mango.domain.contents.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ContentResponseDto {
    private Long contentId;
    private String contentName;
    private String category;
    private String subCategory;
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
    private int NutriFat;

    public ContentResponseDto(Long contentId,
                              String contentName,
                              String category,
                              String subCategory,
                              String brandName,
                              int count,
                              LocalDateTime regDate,
                              LocalDateTime expDate,
                              String storageArea,
                              String memo,
                              String nutriUnit,
                              int nutriCapacity,
                              int nutriKcal,
                              int nutriCarbohydrate,
                              int nutriProtein,
                              int nutriFat)
    {
        this.contentId = contentId;
        this.contentName = contentName;
        this.category = category;
        this.subCategory = subCategory;
        this.brandName = brandName;
        this.count = count;
        this.regDate = regDate;
        this.expDate = expDate;
        this.storageArea = storageArea;
        this.memo = memo;
        NutriUnit = nutriUnit;
        NutriCapacity = nutriCapacity;
        NutriKcal = nutriKcal;
        NutriCarbohydrate = nutriCarbohydrate;
        NutriProtein = nutriProtein;
        NutriFat = nutriFat;
    }
}
