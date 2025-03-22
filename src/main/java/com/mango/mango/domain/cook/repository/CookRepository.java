package com.mango.mango.domain.cook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mango.mango.domain.cook.entity.Cook;
import com.mango.mango.domain.groups.entity.Group;

public interface CookRepository extends JpaRepository<Cook, Long>{
    List<Cook> findByGroup(Group group);
}
