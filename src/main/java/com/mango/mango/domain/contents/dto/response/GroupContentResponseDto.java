package com.mango.mango.domain.contents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupContentResponseDto {
    private Long contentId;
    private String contentName;
    private String brandName;
    private int count;
    private LocalDateTime expDate;
    private String storageArea;
}
