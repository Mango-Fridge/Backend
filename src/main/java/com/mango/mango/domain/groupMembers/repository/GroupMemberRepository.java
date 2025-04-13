package com.mango.mango.domain.groupMembers.repository;

import com.mango.mango.domain.groupMembers.entity.GroupMember;
import com.mango.mango.domain.groups.entity.Group;
import com.mango.mango.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    @Query("SELECT gm.group FROM GroupMember gm WHERE gm.user.id = :userId")
    Optional<Group> findGroupByUserId(@Param("userId") Long userId);

    @Query("SELECT gm.user FROM GroupMember gm WHERE gm.group.id = :groupId")
    List<User> getUsersByGroupId(@Param("groupId") Long groupId);

    long countByGroup(Group group);

    Optional<GroupMember> findByGroupAndUser(Group group, User user);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM GroupMember gm WHERE gm.group = :group AND gm.user = :user")
    void deleteByGroupAndUser(Group group, User user);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM GroupMember gm WHERE gm.user = :user")
    void deleteByUser(@Param("user") User user);
}