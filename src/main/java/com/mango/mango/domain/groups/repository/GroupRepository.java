package com.mango.mango.domain.groups.repository;

import com.mango.mango.domain.groups.entity.Group;
import com.mango.mango.domain.user.entity.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByGroupOwner(User user);
}