package com.mango.mango.domain.item.repository;

import com.mango.mango.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByItemNameContainingIgnoreCase(String keyword, Pageable pageable);
}