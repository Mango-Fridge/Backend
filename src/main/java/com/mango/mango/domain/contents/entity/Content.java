package com.mango.mango.domain.contents.entity;

import com.mango.mango.domain.base.entity.BaseEntity;
import com.mango.mango.domain.groups.entity.Group;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "contents")
public class Content extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTENT_ID")
    private Long contentId;

    @Column(name = "CONTENT_NM", nullable = false, length = 100)
    private String contentName;

    @Column(name = "CATEGORY", nullable = false, length = 50)
    private String category;

    @Column(name = "SUB_CATEGORY", nullable = false, length = 50)
    private String subCategory;

    @Column(name = "BRD_NM", nullable = false, length = 100)
    private String brandName;

    @Column(name = "COUNT", nullable = false)
    private int count;

    @Column(name = "REG_DATE", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "EXP_DATE", nullable = false)
    private LocalDateTime expDate;

    @Column(name = "STORAGE_AREA", nullable = false, length = 50)
    private String storageArea;

    @Column(name = "MEMO")
    private String memo;

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

    @ManyToOne
    @JoinColumn(name = "GRP_ID")
    private Group group;

    @Builder
    public Content(Long contentId, String contentName, int count, LocalDateTime regDate, String storageArea, String subCategory, int nutriKcal) {
        this.contentId = contentId;
        this.contentName = contentName;
        this.count = count;
        this.regDate = regDate;
        this.storageArea = storageArea;
        this.subCategory = subCategory;
        this.nutriKcal = nutriKcal;
    }

    @Builder
    public Content(String contentName,
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
        this.contentName = contentName;
        this.category = category;
        this.subCategory = subCategory;
        this.brandName = brandName;
        this.count = count;
        this.regDate = regDate;
        this.expDate = expDate;
        this.storageArea = storageArea;
        this.memo = memo;
        this.nutriUnit = nutriUnit;
        this.nutriCapacity = nutriCapacity;
        this.nutriKcal = nutriKcal;
        this.nutriCarbohydrate = nutriCarbohydrate;
        this.nutriProtein = nutriProtein;
        this.nutriFat = nutriFat;
    }
}