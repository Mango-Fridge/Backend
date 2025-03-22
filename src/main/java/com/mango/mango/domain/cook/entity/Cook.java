package com.mango.mango.domain.cook.entity;

import java.util.ArrayList;
import java.util.List;

import com.mango.mango.domain.base.entity.BaseEntity;
import com.mango.mango.domain.cookItem.entity.CookItem;
import com.mango.mango.domain.groups.entity.Group;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cooks")
public class Cook extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COOK_ID")
    private Long cookId;

    @Column(name = "COOK_NM", length = 100, nullable = false)
    private String cookName;

    @Column(name = "COOK_MEMO", length = 100, nullable = false)
    private String cookMemo;

    @Column(name = "COOK_NUTRI_KCAL", length = 10, nullable = false)
    private Integer cookNutriKcal;

    @Column(name = "COOK_NUTRI_CARBOHYDRATE", length = 10, nullable = false)
    private Integer cookNutriCarbohydrate;
    
    @Column(name = "COOK_NUTRI_PROTEIN", length = 10, nullable = false)
    private Integer cookNutriProtein;

    @Column(name = "COOK_NUTRI_FAT", length = 10, nullable = false)
    private Integer cookNutriFat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRP_ID", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "cook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CookItem> cookItems = new ArrayList<>();

    @Builder
    public Cook(String cookName, String cookMemo, Integer cookNutriKcal, Integer cookNutriCarbohydrate,
                Integer cookNutriProtein, Integer cookNutriFat, Group group) {
        this.cookName = cookName;
        this.cookMemo = cookMemo;
        this.cookNutriKcal = cookNutriKcal;
        this.cookNutriCarbohydrate = cookNutriCarbohydrate;
        this.cookNutriProtein = cookNutriProtein;
        this.cookNutriFat = cookNutriFat;
        this.group = group;
    }
}
