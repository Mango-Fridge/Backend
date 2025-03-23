package com.mango.mango.domain.groups.service.impl;

import com.mango.mango.domain.groupMembers.entity.GroupMember;
import com.mango.mango.domain.groupMembers.repository.GroupMemberRepository;
import com.mango.mango.domain.groups.dto.reqeust.CreateGroupRequestDto;
import com.mango.mango.domain.groups.dto.reqeust.GroupRequestDto;
import com.mango.mango.domain.groups.dto.response.GroupExistResponseDto;
import com.mango.mango.domain.groups.dto.response.GroupInfoResponseDto;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private final RedisTemplate<String, String> redisTemplate;


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
        if(!(Integer.parseInt(groupCodeSplit[1]) == group.getCreatedAt().getSecond()))     throw new CustomException(ErrorCode.GROUP_INVALID_TIMESTAMP);

        GroupExistResponseDto groupExistResponseDto = GroupExistResponseDto.builder()
                .groupName(group.getGroupName())
                .groupOwnerName(group.getGroupOwner().getUsername())
                .groupMemberCount(group.getGroupMembers().size())
                .build();

        return ResponseEntity.ok(ApiResponse.success(groupExistResponseDto));
    }


    // [5] 그룹 - 그룹 참여하기
    @Transactional
    @Override
    public ResponseEntity<ApiResponse<?>> joinGroup(GroupRequestDto req) {
        Long userId = req.getUserId();
        Long groupId = req.getGroupId();

        // 유저 존재 여부 확인
        User groupUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 그룹 존재 여부 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        // groupId별 userId로 저장(만료일자 7일)
        redisTemplate.opsForSet().add("groupId:" + groupId, userId.toString());
        redisTemplate.opsForSet().add("userId:" + userId, groupId.toString());
        redisTemplate.expire("groupId:" + groupId, 7, TimeUnit.DAYS);
        redisTemplate.expire("userId:" + userId, 7, TimeUnit.DAYS);

        return ResponseEntity.ok(ApiResponse.success(null));
    }


    // [5] 그룹 - 그룹 정보 가져오기
    @Override
    public ResponseEntity<ApiResponse<GroupInfoResponseDto>> getGroupInfo(GroupRequestDto req) {
        Long groupId = req.getGroupId();
        Long userId = req.getUserId();

        // 유저 존재 여부 확인
        User visitUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 그룹 존재 여부 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        // 그룹 코드 생성
        String groupCode = String.format("GRP-%02d-%05d", group.getCreatedAt().getSecond(), groupId);

        // Redis에 저장된 그룹별 신청 유저들의 ID
        Set<Long> groupHopeUserIds = redisTemplate.opsForSet().members("groupId:" + groupId)
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        // 현재 유저가 신청 목록에 포함된 경우, groupUsers와 groupHopeUsers를 null로 설정하고 즉시 반환
        if (groupHopeUserIds.contains(userId)) {
            return ResponseEntity.ok(ApiResponse.success(
                    GroupInfoResponseDto.builder()
                            .groupId(groupId)
                            .groupCode(groupCode)
                            .groupName(group.getGroupName())
                            .groupOwnerId(group.getGroupOwner().getId())
                            .groupUsers(null)
                            .groupHopeUsers(null)
                            .build()
            ));
        }

        // 신청한 유저 리스트
        List<GroupInfoResponseDto.GroupHopeUser> groupHopeUsers = userRepository.findAllById(groupHopeUserIds)
                .stream()
                .map(user -> new GroupInfoResponseDto.GroupHopeUser(user.getId(), user.getUsername()))
                .toList();

        // 그룹 소속 유저
        List<GroupInfoResponseDto.GroupUser> groupUsers = groupMemberRepository.getUsersByGroupId(groupId)
                .stream()
                .map(user -> new GroupInfoResponseDto.GroupUser(user.getId(), user.getUsername()))
                .toList();

        // 그룹에 속해있지 않은 유저인 경우 처리
        if (groupUsers.stream().noneMatch(user -> user.getUserId().equals(userId)))     throw new CustomException(ErrorCode.USER_NOY_ALREADY_IN_GROUP);

        GroupInfoResponseDto groupInfoResponseDto = GroupInfoResponseDto.builder()
                .groupId(groupId)
                .groupCode(groupCode)
                .groupName(group.getGroupName())
                .groupOwnerId(group.getGroupOwner().getId())
                .groupUsers(groupUsers)
                .groupHopeUsers(groupHopeUsers)
                .build();

        return ResponseEntity.ok(ApiResponse.success(groupInfoResponseDto));
    }


    // [5] 그룹 - 그룹 나가기
    @Transactional
    @Override
    public ResponseEntity<ApiResponse<?>> deleteGroupMember(GroupRequestDto req) {
        Long groupId = req.getGroupId();
        Long userId = req.getUserId();
        String key = "groupId:" + groupId;

        // 유저 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 그룹 존재 여부 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        // 그룹장인 경우
        if(group.getGroupOwner().getId() == userId)     throw new CustomException(ErrorCode.GROUP_OWNER_CANNOT_LEAVE);

        // Redis에 저장된 그룹별 신청 유저들의 ID
        Set<Long> groupHopeUserIds = redisTemplate.opsForSet().members(key)
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        // 신청 유저에 속해있는 경우
        if(groupHopeUserIds.contains(userId)){
            redisTemplate.opsForSet().remove(key, userId.toString());
            return ResponseEntity.ok(ApiResponse.success(null));
        }

        // 그룹에 속해있는 경우
        groupMemberRepository.deleteByGroupAndUser(group, user);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}