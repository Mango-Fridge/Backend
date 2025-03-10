package com.mango.mango.domain.contents.repository;

import com.mango.mango.domain.contents.entity.Content;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    @Query("SELECT c FROM Content c WHERE c.group.groupId = :groupId")
    List<Content> getContentsByGroupId(@Param("groupId") Long groupId);
}