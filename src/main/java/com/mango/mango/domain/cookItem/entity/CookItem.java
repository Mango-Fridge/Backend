package com.mango.mango.domain.cookItem.entity;

import com.mango.mango.domain.base.entity.BaseEntity;
import com.mango.mango.domain.cook.entity.Cook;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cook_items")
public class CookItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long itemId;

    @Column(name = "ITEM_NM", nullable = false, length = 100)
    private String itemName;

    @Column(name = "COUNT", nullable = false, length = 100)
    private int count;

    @Column(name = "CATEGORY", nullable = false, length = 50)
    private String category;

    @Column(name = "BRD_NM", nullable = false, length = 100)
    private String brandName;

    @Column(name = "STORAGE_AREA", nullable = false, length = 50)
    private String storageArea;

    @Column(name = "NUTRI_UNIT", length = 10)
    private String nutriUnit;

    @Column(name = "NUTRI_CAPACITY")
    private int nutriCapacity;

    @Column(name = "NUTRI_KCAL")
    private int nutriKcal;

    @Column(name = "SUB_CATEGORY", length = 50)
    private String subCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COOK_ID", nullable = false)
    private Cook cook;

    @Builder
    public CookItem(String itemName,
                int count,
                String category,
                String brandName,
                String storageArea,
                String nutriUnit,
                int nutriCapacity,
                int nutriKcal,
                String subCategory,
                Cook cook) {
        this.itemName = itemName;
        this.count = count;
        this.category = category;
        this.brandName = brandName;
        this.storageArea = storageArea;
        this.nutriUnit = nutriUnit;
        this.nutriCapacity = nutriCapacity;
        this.nutriKcal = nutriKcal;
        this.subCategory = subCategory;
        this.cook = cook;
    }

    public CookItem(
            Long itemId,
            String itemName,
            int count,
            String category,
            String brandName,
            String nutriUnit,
            int nutriCapacity,
            int nutriKcal,
            String subCategory,
            Cook cook
    ) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.count = count;
        this.category = category;
        this.brandName = brandName;
        this.nutriUnit = nutriUnit;
        this.nutriCapacity = nutriCapacity;
        this.nutriKcal = nutriKcal;
        this.subCategory = subCategory;
        this.cook = cook;
    }
}
