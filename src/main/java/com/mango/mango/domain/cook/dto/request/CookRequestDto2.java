package com.mango.mango.domain.cook.dto.request;

import java.util.List;

import com.mango.mango.domain.cookItem.dto.request.CookItemRequestDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookRequestDto2 {
    @NotNull(message = "요리 이름은 필수입니다.")
    @Size(max = 100, message = "요리 이름은 최대 100자까지 가능합니다.")
    private String cookName;

    @Size(max = 255, message = "메모는 최대 255자까지 가능합니다.")
    private String cookMemo;

    @NotNull(message = "칼로리 정보는 필수입니다.")
    private Integer cookNutriKcal;

    @NotNull(message = "탄수화물 정보는 필수입니다.")
    private Integer cookNutriCarbohydrate;

    @NotNull(message = "단백질 정보는 필수입니다.")
    private Integer cookNutriProtein;

    @NotNull(message = "지방 정보는 필수입니다.")
    private Integer cookNutriFat;

    @NotNull(message = "그룹 ID는 필수입니다.")
    private Long groupId;

    private List<CookItemRequestDto> cookItems;
}
