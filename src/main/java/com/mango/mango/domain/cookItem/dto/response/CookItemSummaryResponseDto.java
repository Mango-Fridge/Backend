package com.mango.mango.domain.cookItem.dto.response;

import com.mango.mango.domain.cookItem.entity.CookItem;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookItemSummaryResponseDto {
    private Long cookItemId;
    private String cookItemName;

    public static CookItemSummaryResponseDto fromEntity(CookItem cookItem) {
        return CookItemSummaryResponseDto.builder()
                .cookItemId(cookItem.getItemId())
                .cookItemName(cookItem.getItemName())
                .build();
    }
}
