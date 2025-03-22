package com.mango.mango.domain.contents.service.impl;

import com.mango.mango.domain.contents.dto.request.ContentRequestDto;
import com.mango.mango.domain.contents.dto.response.ContentResponseDto;
import com.mango.mango.domain.contents.dto.response.GroupContentResponseDto;
import com.mango.mango.domain.contents.entity.Content;
import com.mango.mango.domain.contents.repository.ContentRepository;
import com.mango.mango.domain.contents.service.ContentService;
import com.mango.mango.domain.groups.repository.GroupRepository;
import com.mango.mango.global.error.CustomException;
import com.mango.mango.global.error.ErrorCode;
import com.mango.mango.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentServiceImpl implements ContentService {
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private GroupRepository groupRepository;


    // [3] 메인화면 - 메인화면 냉장고 그룹에 따른 내용물 노출
    @Override
    public ResponseEntity<ApiResponse<List<GroupContentResponseDto>>> getContentsByGroupId(Long groupId) {
        boolean existsById = groupRepository.existsById(groupId);
        if(!existsById)     throw new CustomException(ErrorCode.GROUP_NOT_FOUND);

        List<Content> contents = contentRepository.getContentsByGroupId(groupId);

        List<GroupContentResponseDto> GroupContentResponseDtos = contents.stream()
                .map(content -> new GroupContentResponseDto(
                        content.getContentId(),
                        content.getContentName(),
                        content.getCount(),
                        content.getExpDate(),
                        content.getStorageArea()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(GroupContentResponseDtos));
    }


    // [3] 메인화면 - 메인화면 냉장고 내용물 수량 조절
    @Transactional
    public ResponseEntity<ApiResponse<?>> updateContentCounts(ContentRequestDto req) {
        for (ContentRequestDto.ContentUpdateInfo info : req.getContents()) {
            // contentId 기반으로 Content 조회
            Content content = contentRepository.findById(info.getContentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));

            // 수량 조정
            int updateCnt = content.getCount() + info.getCount();
            if (updateCnt < 0)      throw new CustomException(ErrorCode.INVALID_ITEM_COUNT);

            content.setCount(updateCnt);
            contentRepository.save(content);
        }
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    // [3] 메인화면 - 물품 상세 정보
    @Override
    public ResponseEntity<ApiResponse<ContentResponseDto>> getContentDetail(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));

        ContentResponseDto contentResponseDto = new ContentResponseDto(
                content.getContentId(),
                content.getContentName(),
                content.getCategory(),
                content.getSubCategory(),
                content.getBrandName(),
                content.getCount(),
                content.getRegDate(),
                content.getExpDate(),
                content.getStorageArea(),
                content.getMemo(),
                content.getNutriUnit(),
                content.getNutriCapacity(),
                content.getNutriKcal(),
                content.getNutriCarbohydrate(),
                content.getNutriProtein(),
                content.getNutriFat()
        );
        return ResponseEntity.ok(ApiResponse.success(contentResponseDto));
    }
}