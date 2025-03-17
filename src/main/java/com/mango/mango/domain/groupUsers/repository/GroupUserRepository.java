package com.mango.mango.domain.groupUsers.repository;

import com.mango.mango.domain.groupUsers.entity.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
}
