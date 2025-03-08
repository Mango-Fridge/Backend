package com.mango.mango.domain.contents.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ContentRequestDto {
    private List<ContentUpdateInfo> contents;

    @Getter
    @Setter
    public static class ContentUpdateInfo {
        private Long groupId;
        private Long contentId;
        private int count;
    }
}
