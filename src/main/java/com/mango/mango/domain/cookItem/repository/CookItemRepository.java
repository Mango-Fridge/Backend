package com.mango.mango.domain.cookItem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mango.mango.domain.cookItem.entity.CookItem;

public interface CookItemRepository extends JpaRepository<CookItem, Long> {
    List<CookItem> findByCook_CookId(Long cookId);
}
