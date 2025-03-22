package com.mango.mango.domain.groupMembers.repository;

import com.mango.mango.domain.groupMembers.entity.GroupMember;
import com.mango.mango.domain.groups.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<Group> findGroupByUserId(Long userId);
}