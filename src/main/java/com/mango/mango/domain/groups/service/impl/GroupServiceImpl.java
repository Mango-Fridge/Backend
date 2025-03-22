package com.mango.mango.domain.groups.service.impl;

import com.mango.mango.domain.groupMembers.entity.GroupMember;
import com.mango.mango.domain.groupMembers.repository.GroupMemberRepository;
import com.mango.mango.domain.groups.dto.reqeust.CreateGroupRequestDto;
import com.mango.mango.domain.groups.dto.response.GroupExistResponseDto;
import com.mango.mango.domain.groups.dto.response.GroupResponseDto;
import com.mango.mango.domain.groups.entity.Group;
import com.mango.mango.domain.groups.repository.GroupRepository;
import com.mango.mango.domain.groups.service.GroupService;
import com.mango.mango.domain.user.entity.User;
import com.mango.mango.domain.user.repository.UserRepository;
import com.mango.mango.global.error.CustomException;
import com.mango.mango.global.error.ErrorCode;
import com.mango.mango.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;


    // [3] 메인화면 - 냉장고 그룹 불러오기
    @Override
    public ResponseEntity<ApiResponse<GroupResponseDto>> getGroupsByUserId(Long userId) {
        boolean existsById = userRepository.existsById(userId);
        if(!existsById)     throw new CustomException(ErrorCode.USER_NOT_FOUND);

        Group group = groupMemberRepository.findGroupByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        GroupResponseDto groupResponseDto = GroupResponseDto.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .build();

        return ResponseEntity.ok(ApiResponse.success(groupResponseDto));
    }


    // [5] 그룹 - 그룹 생성
    @Transactional
    @Override
    public ResponseEntity<ApiResponse<?>> createGroup(CreateGroupRequestDto req) {
        Long userId = req.getUserId();
        String groupName = req.getGroupName();

        // 유저 존재 여부 확인
        User groupOwner = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 그룹 생성 및 저장
        Group newGroup = Group.builder()
                .groupName(groupName)
                .groupOwner(groupOwner)
                .build();

        // 그룹장을 그룹 멤버로 추가
        GroupMember groupUser = GroupMember.builder()
                .group(newGroup)
                .user(groupOwner)
                .build();

        groupRepository.save(newGroup);
        groupMemberRepository.save(groupUser);

        return ResponseEntity.ok(ApiResponse.success(null));
    }


    // [5] 그룹 - 그룹 존재 여부 확인 (유효성)
    public ResponseEntity<ApiResponse<GroupExistResponseDto>> existGroupByCode(String groupCode) {
        // GRP-49-00001를 "-" 기준으로 나누기
        String[] groupCodeSplit = groupCode.split("-");

        // 그룹코드[GRP] 처리
        if(!groupCodeSplit[0].equals("GRP"))    throw new CustomException(ErrorCode.GROUP_INVALID_PREFIX);

        // 그룹코드[00001] 처리
        Long groupId = Long.valueOf(groupCodeSplit[2]);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        // 그룹코드[49] 처리
        if(Integer.parseInt(groupCodeSplit[1]) == group.getCreatedAt().getSecond())     throw new CustomException(ErrorCode.GROUP_INVALID_TIMESTAMP);

        GroupExistResponseDto groupExistResponseDto = GroupExistResponseDto.builder()
                .groupName(group.getGroupName())
                .groupOwnerName(group.getGroupOwner().getUsername())
                .groupMemberCount(group.getGroupMembers().size())
                .build();

        return ResponseEntity.ok(ApiResponse.success(groupExistResponseDto));
    }
}