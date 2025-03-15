package com.mango.mango.domain.groups.service.impl;

import com.mango.mango.domain.groups.dto.response.GroupResponseDto;
import com.mango.mango.domain.groups.entity.Group;
import com.mango.mango.domain.groups.repository.GroupRepository;
import com.mango.mango.domain.user.repository.UserRepository;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GroupServiceImpl groupService;

    private Group group1;
    private Group group2;
    private List<Group> groupList;

    @BeforeEach
    public void setUp() {
        // 테스트용 그룹 생성
        group1 = new Group(1L, "Group 1");
        group2 = new Group(2L, "Group 2");
        groupList = Arrays.asList(group1, group2);
    }

    @DisplayName("UserId로 그룹 조회 - user가 존재하지 않는 경우")
    @Test
    public void getGroupsByUserId_User가_없는_경우() {
        // Given
        when(userRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        CustomException exception = assertThrows(CustomException.class,
                () -> groupService.getGroupsByUserId(anyLong()));

        // 예외 메시지 검증
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


    @DisplayName("UserId로 그룹 조회 - 그룹이 없는 경우")
    @Test
    public void getGroupsByUserId_그룹이_없는_경우() {
        // Given
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(groupRepository.findByGroupMembersUserId(anyLong())).thenReturn(List.of());

        // When & Then
        ResponseEntity<ApiResponse<List<GroupResponseDto>>> response = groupService.getGroupsByUserId(1L);

        // 예외 메시지 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getData().isEmpty());
    }


    @DisplayName("UserId로 그룹 조회 - 성공한 경우")
    @Test
    public void getGroupsByUserId_성공한_경우() {
        // Given
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(groupRepository.findByGroupMembersUserId(anyLong())).thenReturn(groupList);

        // When & Then
        ResponseEntity<ApiResponse<List<GroupResponseDto>>> response = groupService.getGroupsByUserId(1L);

        // 예외 메시지 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getData().size());
        assertEquals("Group 1", response.getBody().getData().get(0).getGroupName());
        assertEquals("Group 2", response.getBody().getData().get(1).getGroupName());
    }
}