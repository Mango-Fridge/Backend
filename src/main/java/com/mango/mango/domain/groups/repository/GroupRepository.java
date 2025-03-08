package com.mango.mango.domain.groups.repository;

import com.mango.mango.domain.groups.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByGroupMembersUserId(Long userId);
}