package com.mango.mango.domain.groups.controller;

import com.mango.mango.domain.groups.dto.reqeust.CreateGroupRequestDto;
import com.mango.mango.domain.groups.dto.response.GroupExistResponseDto;
import com.mango.mango.domain.groups.dto.response.GroupResponseDto;
import com.mango.mango.domain.groups.service.GroupService;
import com.mango.mango.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Tag(name = "Group", description = "그룹 관련 API")
public class GroupController {
    private final GroupService groupService;

    @Operation(summary = "[3] 메인화면 - 냉장고 그룹 불러오기")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<GroupResponseDto>> getGroupsByUserId(@PathVariable Long userId) {
        return groupService.getGroupsByUserId(userId);
    }

    @Operation(summary = "[5] 그룹 - 그룹 생성")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createGroup(@RequestBody CreateGroupRequestDto req) {
        return groupService.createGroup(req);
    }

    @Operation(summary = "[5] 그룹 - 그룹 존재 여부 확인 (유효성)")
    @GetMapping("/exist/{groupCode}")
    public ResponseEntity<ApiResponse<GroupExistResponseDto>> existGroupByCode(@PathVariable String groupCode) {
        return groupService.existGroupByCode(groupCode);
    }
}