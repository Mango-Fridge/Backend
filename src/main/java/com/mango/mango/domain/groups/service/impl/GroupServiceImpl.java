package com.mango.mango.domain.groups.service.impl;

import com.mango.mango.domain.groups.dto.response.GroupResponseDto;
import com.mango.mango.domain.groups.entity.Group;
import com.mango.mango.domain.groups.repository.GroupRepository;
import com.mango.mango.domain.groups.service.GroupService;
import com.mango.mango.domain.user.repository.UserRepository;
import com.mango.mango.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    // 냉장고 그룹 불러오기
    @Override
    public ResponseEntity<ApiResponse<List<GroupResponseDto>>> getGroupsByUserId(Long userId) {
        boolean existsById = userRepository.existsById(userId);
        if(!existsById){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.error(
                                    "404",
                                    "존재하지 않는 User ID: " + userId
                            )
                    );
        }

        List<Group> groups = groupRepository.findByGroupMembersUserId(userId);

        List<GroupResponseDto> groupResponseDtos = groups.stream()
                .map(group -> new GroupResponseDto(
                        group.getGroupId(),
                        group.getGroupName()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(groupResponseDtos));
    }
}