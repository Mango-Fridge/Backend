package com.mango.mango.domain.item.entity;

import com.mango.mango.domain.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long itemId;

    @Column(name = "ITEM_NM", nullable = false, length = 100)
    private String itemName;

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

    @Column(name = "NUTRI_CARBOHYDRATE")
    private int nutriCarbohydrate;

    @Column(name = "NUTRI_PROTEIN")
    private int nutriProtein;

    @Column(name = "NUTRI_FAT")
    private int nutriFat;

    @Builder
    public Item(String itemName,
                String category,
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