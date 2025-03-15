package com.mango.mango.domain.item.service.impl;

import com.mango.mango.domain.contents.entity.Content;
import com.mango.mango.domain.contents.repository.ContentRepository;
import com.mango.mango.domain.groups.entity.Group;
import com.mango.mango.domain.groups.repository.GroupRepository;
import com.mango.mango.domain.item.dto.request.ItemRequestDto;
import com.mango.mango.domain.item.dto.response.ItemResponseDto;
import com.mango.mango.domain.item.dto.response.SearchItemResponseDto;
import com.mango.mango.domain.item.entity.Item;
import com.mango.mango.domain.item.repository.ItemRepository;
import com.mango.mango.global.error.CustomException;
import com.mango.mango.global.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item mockItem;

    @BeforeEach
    void setUp() {
        mockItem = new Item(
                1L,
                "Test Item",
                "Test Category",
                "Test Brand",
                "mg",
                100,
                200,
                50,
                30,
                10
        );
    }

    @DisplayName("Keyword로 아이템 조회 - 성공한 경우")
    @Test
    void searchItems_성공한_경우() {
        // Given
        String keyword = "Test";
        when(itemRepository.findByItemNameContainingIgnoreCase(keyword)).thenReturn(List.of(mockItem));

        // When
        ResponseEntity<ApiResponse<SearchItemResponseDto>> response = itemService.searchItems(keyword);

        // Then
        assertThat(response.getBody().getData().getItems()).hasSize(1);
        assertThat(response.getBody().getData().getItems().get(0).getItemName()).isEqualTo("Test Item");

        verify(itemRepository, times(1)).findByItemNameContainingIgnoreCase(keyword);
    }


    @DisplayName("ItemId로 아이템 상세 조회 - 성공한 경우")
    @Test
    void getItemById_성공한_경우() {
        // Given
        Long itemId = 1L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(mockItem));

        // When
        ResponseEntity<ApiResponse<ItemResponseDto>> response = itemService.getItemById(itemId);

        // Then
        assertThat(response.getBody().getData().getItemId()).isEqualTo(1L);
        assertThat(response.getBody().getData().getItemName()).isEqualTo("Test Item");

        verify(itemRepository, times(1)).findById(itemId);
    }


    @DisplayName("ItemId로 아이템 상세 조회 - Item이 존재하지 않는 경우")
    @Test
    void getItemById_Item이_없는_경우() {
        // Given
        Long itemId = 2L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CustomException.class, () -> itemService.getItemById(itemId));

        verify(itemRepository, times(1)).findById(itemId);
    }


    @DisplayName("아이템 추가 - 성공한 경우")
    @Test
    void addItem_성공한_경우() {
        // Given
        ItemRequestDto requestDto = new ItemRequestDto(
                1L, "New Item", "Category", "Brand", "Fridge",
                "g", 150, 300, 60, 40, 20, true // isOpenItem = true
        );

        Group mockGroup = new Group(1L, "Test Group");

        when(groupRepository.findById(1L)).thenReturn(Optional.of(mockGroup));
        when(itemRepository.save(any(Item.class))).thenReturn(mockItem);
        when(contentRepository.save(any(Content.class))).thenReturn(new Content());

        // When
        ResponseEntity<ApiResponse<?>> response = itemService.addItem(requestDto);

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(contentRepository, times(1)).save(any(Content.class));
    }


    @DisplayName("아이템 추가 - 그룹이 존재하지 않는 경우")
    @Test
    void addItem_Group이_없는_경우() {
        // Given
        ItemRequestDto requestDto = new ItemRequestDto(
                2L, null, null, null, null, null,
                0, 0, 0, 0, 0, false
        );

        when(groupRepository.findById(2L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CustomException.class, () -> itemService.addItem(requestDto));

        verify(groupRepository, times(1)).findById(2L);
        verify(itemRepository, never()).save(any(Item.class));
        verify(contentRepository, never()).save(any(Content.class));
    }
}