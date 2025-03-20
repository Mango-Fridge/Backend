package com.mango.mango.domain.cookItem.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mango.mango.domain.cookItem.dto.response.CookItemResponseDto;
import com.mango.mango.domain.cookItem.entity.CookItem;
import com.mango.mango.domain.cookItem.repository.CookItemRepository;
import com.mango.mango.domain.cookItem.service.CookItemService;

@Service
public class CookItemServiceImpl implements CookItemService{

    @Autowired
    private CookItemRepository cookItemRepository;

    @Override
    public List<CookItemResponseDto> getCookItemsByCookId(Long cookId) {
        List<CookItem> cookItems = cookItemRepository.findByCook_CookId(cookId);
        return cookItems.stream()
                .map(CookItemResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
