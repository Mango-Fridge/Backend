package com.mango.mango.domain.cook.repository;

import java.util.List;

import com.mango.mango.domain.cook.entity.Cook;
import com.mango.mango.domain.groups.entity.Group;

public interface CookRepository {
    void save(Cook cook);
    List<Cook> findByGroup(Group group);
}
