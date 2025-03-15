package com.mango.mango.domain.contents.service.impl;

import com.mango.mango.domain.contents.dto.request.ContentRequestDto;
import com.mango.mango.domain.contents.dto.response.ContentResponseDto;
import com.mango.mango.domain.contents.entity.Content;
import com.mango.mango.domain.contents.repository.ContentRepository;
import com.mango.mango.domain.groups.repository.GroupRepository;
import com.mango.mango.global.error.CustomException;
import com.mango.mango.global.error.ErrorCode;
import com.mango.mango.global.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ContentServiceImplTest {

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private ContentServiceImpl contentService;

    private Content content1;
    private Content content2;
    private List<Content> contentList;

    @BeforeEach
    public void setUp() {
        // 테스트용 Content 객체 생성
        content1 = new Content(1L, "Content 1", 7, LocalDateTime.now(), "Fridge");
        content2 = new Content(2L, "Content 2", 3, LocalDateTime.now(), "Freezer");
        contentList = Arrays.asList(content1, content2);
    }

    @DisplayName("groupId로 콘텐츠 조회 - 그룹이 존재하지 않는 경우")
    @Test
    public void getContentsByGroupId_그룹이_없는_경우() {
        // Given
        when(groupRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        CustomException exception = assertThrows(CustomException.class,
                () -> contentService.getContentsByGroupId(anyLong()));

        // 예외 메시지 검증
        assertEquals(ErrorCode.GROUP_NOT_FOUND, exception.getErrorCode());
    }


    @DisplayName("groupId로 콘텐츠 조회 - 성공한 경우")
    @Test
    public void getContentsByGroupId_성공한_경우() {
        // Given
        when(groupRepository.existsById(anyLong())).thenReturn(true);
        when(contentRepository.getContentsByGroupId(anyLong())).thenReturn(contentList);

        // When & Then
        ResponseEntity<ApiResponse<List<ContentResponseDto>>> response = contentService.getContentsByGroupId(1L);

        // 예외 메시지 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getData().size());
        assertEquals("Content 1", response.getBody().getData().get(0).getContentName());
        assertEquals("Content 2", response.getBody().getData().get(1).getContentName());
    }


    @DisplayName("콘텐츠 개수 수정 - 콘텐츠가 존재하지 않는 경우")
    @Test
    public void updateContentCounts_콘텐츠가_없는_경우() {
        // Given
        when(contentRepository.findById(anyLong())).thenReturn(Optional.empty());

        ContentRequestDto.ContentUpdateInfo updateInfo = new ContentRequestDto.ContentUpdateInfo(1L, 1L, -6);
        ContentRequestDto requestDto = new ContentRequestDto(Arrays.asList(updateInfo));

        // When & Then
        CustomException exception = assertThrows(CustomException.class,
                () -> contentService.updateContentCounts(requestDto));

        // 예외 메시지 검증
        assertEquals(ErrorCode.CONTENT_NOT_FOUND, exception.getErrorCode());
    }


    @DisplayName("콘텐츠 개수 수정 - 성공한 경우")
    @Test
    public void updateContentCounts_성공한_경우(){
        // Given
        when(contentRepository.findById(1L)).thenReturn(Optional.of(content1));

        ContentRequestDto.ContentUpdateInfo updateInfo = new ContentRequestDto.ContentUpdateInfo(1L, 1L, -6);
        ContentRequestDto requestDto = new ContentRequestDto(Arrays.asList(updateInfo));

        // When & Then
        ResponseEntity<ApiResponse<?>> response = contentService.updateContentCounts(requestDto);

        // 예외 메시지 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody().getData());
        assertEquals(1, content1.getCount()); // -6이 되었는지 확인
    }


    @DisplayName("콘텐츠 개수 수정 - 개수 오류난 경우")
    @Test
    public void updateContentCounts_개수_오류() {
        // Given
        when(contentRepository.findById(1L)).thenReturn(Optional.of(content1));

        ContentRequestDto.ContentUpdateInfo updateInfo = new ContentRequestDto.ContentUpdateInfo(1L, 1L, -8);
        ContentRequestDto requestDto = new ContentRequestDto(Arrays.asList(updateInfo));

        // When & Then
        CustomException exception = assertThrows(CustomException.class,
                () -> contentService.updateContentCounts(requestDto));

        // 예외 메시지 검증
        assertEquals(ErrorCode.INVALID_ITEM_COUNT, exception.getErrorCode());
    }


    @DisplayName("콘텐츠 상세 보기 - 콘텐츠가 존재하지 않는 경우")
    @Test
    public void getContentDetail_콘텐츠가_없는_경우() {
        // Given
        when(contentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class,
                () -> contentService.getContentDetail(anyLong()));

        // 예외 메시지 검증
        assertEquals(ErrorCode.CONTENT_NOT_FOUND, exception.getErrorCode());
    }


    @DisplayName("콘텐츠 상세 보기 - 성공한 경우")
    @Test
    public void getContentDetail_성공한_경우() {
        // Given
        when(contentRepository.findById(anyLong())).thenReturn(Optional.of(content1));

        // When & Then
        ResponseEntity<ApiResponse<ContentResponseDto>> response = contentService.getContentDetail(1L);

        // 예외 메시지 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Content 1", response.getBody().getData().getContentName());
    }
}