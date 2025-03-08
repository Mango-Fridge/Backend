package com.mango.mango.domain.contents.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentRequestDto {
    private List<ContentUpdateInfo> contents;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ContentUpdateInfo {
        private Long groupId;
        private Long contentId;
        private int count;
    }
}