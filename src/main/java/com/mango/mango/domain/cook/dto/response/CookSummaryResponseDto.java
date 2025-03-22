package com.mango.mango.domain.cook.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.mango.mango.domain.cook.entity.Cook;
import com.mango.mango.domain.cookItem.dto.response.CookItemSummaryResponseDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CookSummaryResponseDto {
    private Long cookId;
    private String cookName;
    private List<CookItemSummaryResponseDto> cookItems;

    public static CookSummaryResponseDto fromEntity(Cook cook) {
        return CookSummaryResponseDto.builder()
                .cookId(cook.getCookId())
                .cookName(cook.getCookName())
                .cookItems(cook.getCookItems().stream()
                        .map(CookItemSummaryResponseDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
