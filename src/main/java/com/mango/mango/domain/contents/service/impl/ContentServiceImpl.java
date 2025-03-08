package com.mango.mango.domain.contents.service.impl;

import com.mango.mango.domain.contents.dto.request.ContentRequestDto;
import com.mango.mango.domain.contents.dto.response.ContentResponseDto;
import com.mango.mango.domain.contents.entity.Content;
import com.mango.mango.domain.contents.repository.ContentRepository;
import com.mango.mango.domain.contents.service.ContentService;
import com.mango.mango.domain.groups.repository.GroupRepository;
import com.mango.mango.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentServiceImpl implements ContentService {
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private GroupRepository groupRepository;

    // 메인화면 냉장고 그룹에 따른 내용물 노출
    @Override
    public ResponseEntity<ApiResponse<List<ContentResponseDto>>> getContentsByGroupId(Long groupId) {
        boolean existsById = groupRepository.existsById(groupId);
        if(!existsById){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.error(
                                    "404",
                                    "존재하지 않는 Group ID: " + groupId
                            )
                    );
        }

        List<Content> contents = contentRepository.getContentsByGroupId(groupId);

        List<ContentResponseDto> contentResponseDtos = contents.stream()
                .map(content -> new ContentResponseDto(
                        content.getContentId(),
                        content.getContentName(),
                        content.getCount(),
                        content.getExpDate(),
                        content.getStorageArea()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(contentResponseDtos));
    }

    // 메인화면 냉장고 내용물 수량 조절
    @Transactional
    public ResponseEntity<ApiResponse<?>> updateContentCounts(ContentRequestDto req) {
        for (ContentRequestDto.ContentUpdateInfo info : req.getContents()) {
            // contentId 기반으로 Content 조회
            Optional<Content> contentOpt = contentRepository.findById(info.getContentId());

            if (contentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                ApiResponse.error(
                                        "404",
                                        "존재하지 않는 컨텐츠 ID: " + info.getContentId()
                                )
                        );
            }

            Content content = contentOpt.get();

            // 수량 조정
            int updateCnt = content.getCount() + info.getCount();
            if (updateCnt < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(
                                ApiResponse.error(
                                        "400",
                                        "품목 개수 오류."
                                )
                        );
            }

            content.setCount(updateCnt);
            contentRepository.save(content);
        }
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 물품 상세 정보
    @Override
    public ResponseEntity<ApiResponse<ContentResponseDto>> getContentDetail(Long contentId) {
        Optional<Content> contentOpt = contentRepository.findById(contentId);
        if (contentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.error(
                                    "404",
                                    "존재하지 않는 Content ID: " + contentId
                            )
                    );
        }

        Content content = contentOpt.get();

        ContentResponseDto contentResponseDto = new ContentResponseDto(
                content.getContentId(),
                content.getContentName(),
                content.getCategory(),
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