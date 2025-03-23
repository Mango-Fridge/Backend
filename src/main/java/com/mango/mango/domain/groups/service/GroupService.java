package com.mango.mango.domain.groups.service;

import com.mango.mango.domain.groups.dto.reqeust.CreateGroupRequestDto;
import com.mango.mango.domain.groups.dto.reqeust.GroupRequestDto;
import com.mango.mango.domain.groups.dto.response.GroupExistResponseDto;
import com.mango.mango.domain.groups.dto.response.GroupInfoResponseDto;
import com.mango.mango.domain.groups.dto.response.GroupResponseDto;
import com.mango.mango.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface GroupService {
    ResponseEntity<ApiResponse<GroupResponseDto>> getGroupsByUserId(Long userId);
    ResponseEntity<ApiResponse<?>> createGroup(CreateGroupRequestDto req);
    ResponseEntity<ApiResponse<GroupExistResponseDto>> existGroupByCode(String groupCode);
    ResponseEntity<ApiResponse<?>> joinGroup(GroupRequestDto req);
    ResponseEntity<ApiResponse<GroupInfoResponseDto>> getGroupInfo(GroupRequestDto req);
}