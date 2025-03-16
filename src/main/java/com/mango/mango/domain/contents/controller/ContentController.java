package com.mango.mango.domain.contents.controller;

import com.mango.mango.domain.contents.dto.request.ContentRequestDto;
import com.mango.mango.domain.contents.dto.response.ContentResponseDto;
import com.mango.mango.domain.contents.dto.response.GroupContentResponseDto;
import com.mango.mango.domain.contents.service.ContentService;
import com.mango.mango.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
@Tag(name = "Content", description = "그룹 내 물품 관련 API")
public class ContentController {
    private final ContentService contentService;

    @Operation(summary = "[3] 메인화면 - 메인화면 냉장고 그룹에 따른 내용물 노출")
    @GetMapping("/group/{groupId}")
    public ResponseEntity<ApiResponse<List<GroupContentResponseDto>>> getContentsByGroupId(@PathVariable Long groupId) {
        return contentService.getContentsByGroupId(groupId);
    }

    @Operation(summary = "[3] 메인화면 - 메인화면 냉장고 내용물 수량 조절")
    @PatchMapping("/quantity")
    public ResponseEntity<ApiResponse<?>> updateContents(@RequestBody ContentRequestDto req) {
        return contentService.updateContentCounts(req);
    }

    @Operation(summary = "[3] 메인화면 - 물품 상세 정보")
    @GetMapping("/{contentId}")
    public ResponseEntity<ApiResponse<ContentResponseDto>> getContentDetail(@PathVariable Long contentId) {
        return contentService.getContentDetail(contentId);
    }
}