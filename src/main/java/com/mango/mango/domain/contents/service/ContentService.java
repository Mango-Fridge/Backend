package com.mango.mango.domain.contents.service;

import com.mango.mango.domain.contents.dto.request.ContentRequestDto;
import com.mango.mango.domain.contents.dto.response.ContentResponseDto;
import com.mango.mango.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface ContentService {
    ResponseEntity<ApiResponse<List<ContentResponseDto>>> getContentsByGroupId(Long groupId);
    ResponseEntity<ApiResponse<?>> updateContentCounts(ContentRequestDto req);
    ResponseEntity<ApiResponse<ContentResponseDto>> getContentDetail(Long contentId);
}