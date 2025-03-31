package com.mango.mango.domain.item.entity;

import com.mango.mango.domain.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "items")
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long itemId;

    @EqualsAndHashCode.Include
    @Column(name = "ITEM_NM", nullable = false, length = 100)
    private String itemName;

    @EqualsAndHashCode.Include
    @Column(name = "CATEGORY", nullable = false, length = 50)
    private String category;

    @EqualsAndHashCode.Include
    @Column(name = "SUB_CATEGORY", nullable = false, length = 50)
    private String subCategory;

    @EqualsAndHashCode.Include
    @Column(name = "BRD_NM", nullable = false, length = 100)
    private String brandName;

    @EqualsAndHashCode.Include
    @Column(name = "STORAGE_AREA", length = 50)
    private String storageArea;

    @EqualsAndHashCode.Include
    @Column(name = "NUTRI_UNIT", nullable = false, length = 10)
    private String nutriUnit;

    @EqualsAndHashCode.Include
    @Column(name = "NUTRI_CAPACITY", nullable = false)
    private int nutriCapacity;

    @EqualsAndHashCode.Include
    @Column(name = "NUTRI_KCAL", nullable = false)
    private int nutriKcal;

    @EqualsAndHashCode.Include
    @Column(name = "NUTRI_CARBOHYDRATE", nullable = false)
    private int nutriCarbohydrate;

    @EqualsAndHashCode.Include
    @Column(name = "NUTRI_PROTEIN", nullable = false)
    private int nutriProtein;

    @EqualsAndHashCode.Include
    @Column(name = "NUTRI_FAT", nullable = false)
    private int nutriFat;

    @Builder
    public Item(String itemName,
                String category,
                String subCategory,
                String brandName,
                String storageArea,
                String nutriUnit,
                int nutriCapacity,
                int nutriKcal,
                int nutriCarbohydrate,
                int nutriProtein,
                int nutriFat) {
        this.itemName = itemName;
        this.category = category;
        this.subCategory = subCategory;
        this.brandName = brandName;
        this.storageArea = storageArea;
        this.nutriUnit = nutriUnit;
        this.nutriCapacity = nutriCapacity;
        this.nutriKcal = nutriKcal;
        this.nutriCarbohydrate = nutriCarbohydrate;
        this.nutriProtein = nutriProtein;
        this.nutriFat = nutriFat;
    }

    public Item(
            Long itemId,
            String itemName,
            String category,
            String brandName,
            String nutriUnit,
            int nutriCapacity,
            int nutriKcal,
            int nutriCarbohydrate,
            int nutriProtein,
            int nutriFat
    ) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.brandName = brandName;
        this.nutriUnit = nutriUnit;
        this.nutriCapacity = nutriCapacity;
        this.nutriKcal = nutriKcal;
        this.nutriCarbohydrate = nutriCarbohydrate;
        this.nutriProtein = nutriProtein;
        this.nutriFat = nutriFat;
    }
}