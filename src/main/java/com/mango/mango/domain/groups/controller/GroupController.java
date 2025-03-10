package com.mango.mango.domain.groups.controller;

import com.mango.mango.domain.groups.dto.response.GroupResponseDto;
import com.mango.mango.domain.groups.service.GroupService;
import com.mango.mango.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @Operation(summary = "[3] 메인화면 - 냉장고 그룹 불러오기")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<GroupResponseDto>>> getGroupsByUserId(@PathVariable Long userId) {
        return groupService.getGroupsByUserId(userId);
    }
}