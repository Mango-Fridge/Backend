package com.mango.mango.domain.contents.service.impl;

import com.mango.mango.domain.contents.dto.request.ContentRequestDto;
import com.mango.mango.domain.contents.dto.response.ContentResponseDto;
import com.mango.mango.domain.contents.entity.Content;
import com.mango.mango.domain.contents.repository.ContentRepository;
import com.mango.mango.domain.groups.repository.GroupRepository;
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

    @DisplayName("groupId로 콘텐츠 조회 메소드 - 그룹이 존재하지 않는 경우")
    @Test
    public void getContentsByGroupId_그룹이_없는_경우() {
        when(groupRepository.existsById(anyLong())).thenReturn(false);

        ResponseEntity<ApiResponse<List<ContentResponseDto>>> response = contentService.getContentsByGroupId(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getError().getMessage().contains("존재하지 않는 Group ID"));
    }

    @DisplayName("groupId로 콘텐츠 조회 메소드 - 성공한 경우")
    @Test
    public void getContentsByGroupId_성공한_경우() {
        when(groupRepository.existsById(anyLong())).thenReturn(true);
        when(contentRepository.getContentsByGroupId(anyLong())).thenReturn(contentList);

        ResponseEntity<ApiResponse<List<ContentResponseDto>>> response = contentService.getContentsByGroupId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getData().size());
        assertEquals("Content 1", response.getBody().getData().get(0).getContentName());
        assertEquals("Content 2", response.getBody().getData().get(1).getContentName());
    }

    @DisplayName("콘텐츠 개수 수정 메소드 - 콘텐츠가 존재하지 않는 경우")
    @Test
    public void updateContentCounts_콘텐츠가_없는_경우() {
        when(contentRepository.findById(anyLong())).thenReturn(Optional.empty());

        ContentRequestDto.ContentUpdateInfo updateInfo = new ContentRequestDto.ContentUpdateInfo(1L, 1L, -6);
        ContentRequestDto requestDto = new ContentRequestDto(Arrays.asList(updateInfo));

        ResponseEntity<ApiResponse<?>> response = contentService.updateContentCounts(requestDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().getError().getMessage().contains("존재하지 않는 컨텐츠 ID"));
    }

    @DisplayName("콘텐츠 개수 수정 메소드 - 성공한 경우")
    @Test
    public void updateContentCounts_성공한_경우(){
        when(contentRepository.findById(1L)).thenReturn(Optional.of(content1));

        ContentRequestDto.ContentUpdateInfo updateInfo = new ContentRequestDto.ContentUpdateInfo(1L, 1L, -6);
        ContentRequestDto requestDto = new ContentRequestDto(Arrays.asList(updateInfo));

        ResponseEntity<ApiResponse<?>> response = contentService.updateContentCounts(requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody().getData());
        assertEquals(1, content1.getCount()); // -6이 되었는지 확인
    }

    @DisplayName("콘텐츠 개수 수정 메소드 - 개수 오류난 경우")
    @Test
    public void updateContentCounts_개수_오류() {
        when(contentRepository.findById(1L)).thenReturn(Optional.of(content1));

        ContentRequestDto.ContentUpdateInfo updateInfo = new ContentRequestDto.ContentUpdateInfo(1L, 1L, -8);
        ContentRequestDto requestDto = new ContentRequestDto(Arrays.asList(updateInfo));

        ResponseEntity<ApiResponse<?>> response = contentService.updateContentCounts(requestDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getError().getMessage().contains("품목 개수 오류."));   // 7 - 8 = -1임
    }

    @DisplayName("콘텐츠 상세 보기 메소드 - 콘텐츠가 존재하지 않는 경우")
    @Test
    public void getContentDetail_콘텐츠가_없는_경우() {
        when(contentRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<ContentResponseDto>> response = contentService.getContentDetail(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getError().getMessage().contains("존재하지 않는 Content ID"));
    }

    @DisplayName("콘텐츠 상세 보기 메소드 - 성공한 경우")
    @Test
    public void getContentDetail_성공한_경우() {
        when(contentRepository.findById(anyLong())).thenReturn(Optional.of(content1));

        ResponseEntity<ApiResponse<ContentResponseDto>> response = contentService.getContentDetail(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Content 1", response.getBody().getData().getContentName());
    }
}