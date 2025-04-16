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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
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

        // 이미 그룹이 존재하는지 확인
        if(groupOwner.getGroupMember() != null)     throw new CustomException(ErrorCode.USER_ALREADY_IN_GROUP);

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
                .groupId(groupId)
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
        String groupKey = "groupId:" + groupId;
        String userKey = "userId:" + userId;

        // 유저 존재 여부 확인
        User groupUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 그룹 존재 여부 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        // 이미 그룹이 존재하는지 확인
        if(groupUser.getGroupMember() != null)     throw new CustomException(ErrorCode.USER_ALREADY_IN_GROUP);

        // Redis에 저장된 그룹별 신청 유저들의 ID
        Set<Long> groupHopeUserIds = redisTemplate.opsForSet().members(groupKey)
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        if(groupHopeUserIds.contains(userId))   throw new CustomException(ErrorCode.USER_ALREADY_IN_GROUP_HOPE);

        // groupId별 userId로 저장(만료일자 7일)
        redisTemplate.opsForSet().add(groupKey, userId.toString());
        redisTemplate.opsForSet().add(userKey, groupId.toString());
        redisTemplate.expire(groupKey, 7, TimeUnit.DAYS);
        redisTemplate.expire(userKey, 7, TimeUnit.DAYS);

        return ResponseEntity.ok(ApiResponse.success(null));
    }


    // [5] 그룹 - 그룹 정보 가져오기
    @Override
    public ResponseEntity<ApiResponse<GroupInfoResponseDto>> getGroupInfo(GroupRequestDto req) {
        Long groupId = req.getGroupId();
        Long userId = req.getUserId();
        String groupKey = "groupId:" + groupId;

        // 유저 존재 여부 확인
        User visitUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 그룹 존재 여부 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        // 그룹 코드 생성
        String groupCode = String.format("GRP-%02d-%05d", group.getCreatedAt().getSecond(), groupId);

        // Redis에 저장된 그룹별 신청 유저들의 ID
        Set<Long> groupHopeUserIds = redisTemplate.opsForSet().members(groupKey)
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
        if (groupUsers.stream().noneMatch(user -> user.getUserId().equals(userId)))     throw new CustomException(ErrorCode.USER_NOT_ALREADY_IN_GROUP);

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
        String groupKey = "groupId:" + groupId;
        String userKey = "userId:" + userId;

        // 유저 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        log.info("유저 조회 완료 - userId: {}", userId);

        // 그룹 존재 여부 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
        log.info("그룹 조회 완료 - groupId: {}", groupId);

        // 그룹장인 경우
        if(group.getGroupOwner().getId() == userId){
            log.info("요청한 유저는 그룹장입니다 - userId: {}", userId);
            // 그룹장만 있는 경우
            if(groupMemberRepository.countByGroup(group) == 1){
                log.info("그룹장만 그룹에 존재함. 그룹 삭제 진행 - groupId: {}", groupId);
                groupMemberRepository.deleteByGroupAndUser(group, user);
                groupRepository.delete(group);
                return ResponseEntity.ok(ApiResponse.success(null));
            }
            // 그룹장 외 멤버가 있는 경우
            else{
                log.warn("그룹장 외 다른 멤버가 존재하여 그룹장이 나갈 수 없음 - groupId: {}", groupId);
                throw new CustomException(ErrorCode.GROUP_OWNER_CANNOT_LEAVE);
            }
        }

        // Redis에 저장된 그룹별 신청 유저들의 ID
        Set<Long> groupHopeUserIds = redisTemplate.opsForSet().members(groupKey)
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toSet());
        log.info("Redis에서 그룹 신청 유저 목록 조회 완료 - 신청 유저 수: {}", groupHopeUserIds.size());

        // 신청 유저에 속해있는 경우
        if(groupHopeUserIds.contains(userId)){
            log.info("유저가 신청 유저 목록에 존재 - userId: {}", userId);
            redisTemplate.opsForSet().remove(groupKey, userId.toString());
            redisTemplate.opsForSet().remove(userKey, groupId.toString());
            log.info("Redis에서 신청 유저 정보 제거 완료 - userId: {}, groupId: {}", userId, groupId);
            return ResponseEntity.ok(ApiResponse.success(null));
        }

        // 그룹에 속해있는 경우
        groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_ALREADY_IN_GROUP));
        groupMemberRepository.deleteByGroupAndUser(group, user);
        log.info("유저가 그룹 멤버에서 정상적으로 탈퇴함 - groupId: {}, userId: {}", groupId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    // [5] 그룹 - 그룹장 임명
    @Transactional
    @Override
    public ResponseEntity<ApiResponse<?>> updateGroupOwner(GroupRequestDto req) {
        Long groupId = req.getGroupId();
        Long userId = req.getUserId();

        // 유저 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 그룹 존재 여부 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        // 이미 그룹장인 경우
        if(group.getGroupOwner().equals(user))      throw new CustomException(ErrorCode.GROUP_OWNER_ALREADY_EXISTS);

        // 그룹에 소속된 유저가 아닌 경우
        if (group.getGroupMembers().stream().noneMatch(gm -> gm.getUser().equals(user)))        throw new CustomException(ErrorCode.USER_NOT_ALREADY_IN_GROUP);

        // 그룹장의 변경을 위해, 기존 그룹장과 새로운 그룹장의 관계를 업데이트한다.
        group.setGroupOwner(user);      // 그룹장 변경
        groupRepository.save(group);    // 변경 사항 저장

        return ResponseEntity.ok(ApiResponse.success(null));
    }


    // [5] 그룹 - 그룹 참여 승인 요청 (거절)
    @Override
    public ResponseEntity<ApiResponse<?>> rejectGroupHopeUser(GroupRequestDto req) {
        Long groupId = req.getGroupId();
        Long userId = req.getUserId();
        String groupKey = "groupId:" + groupId;
        String userKey = "userId:" + userId;

        // 유저 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 그룹 존재 여부 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        // Redis에 저장된 그룹별 신청 유저들의 ID
        Set<String> groupHopeUserIds = redisTemplate.opsForSet().members(groupKey);
        if (groupHopeUserIds == null || !groupHopeUserIds.contains(userId.toString())) {
            throw new CustomException(ErrorCode.USER_NOT_ALREADY_IN_GROUP_HOPE);
        }

        // Redis에서 정보 삭제
        redisTemplate.opsForSet().remove(groupKey, userId.toString());
        redisTemplate.opsForSet().remove(userKey, groupId.toString());
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    // [5] 그룹 - 그룹 참여 승인 요청 (승인)
    @Transactional
    @Override
    public ResponseEntity<ApiResponse<?>> approveGroupHopeUser(GroupRequestDto req) {
        Long groupId = req.getGroupId();
        Long userId = req.getUserId();
        String groupKey = "groupId:" + groupId;
        String userKey = "userId:" + userId;

        // 유저 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 그룹 존재 여부 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        // 이미 그룹에 속해있는 유저인 경우
        if(user.getGroupMember() != null)     throw new CustomException(ErrorCode.USER_ALREADY_IN_GROUP);

        // Redis에 저장된 그룹별 신청 유저들의 ID 확인
        Set<String> groupHopeUserIds = redisTemplate.opsForSet().members(groupKey);
        if (groupHopeUserIds == null || !groupHopeUserIds.contains(userId.toString())) {
            throw new CustomException(ErrorCode.USER_NOT_ALREADY_IN_GROUP_HOPE);
        }

        // Redis에서 정보 삭제
        redisTemplate.opsForSet().remove(groupKey, userId.toString());
        redisTemplate.opsForSet().remove(userKey, groupId.toString());

        GroupMember groupMember = new GroupMember(group, user);
        groupMemberRepository.save(groupMember);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}