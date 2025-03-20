package com.mango.mango.domain.cookItem.service;

import java.util.List;

import com.mango.mango.domain.cookItem.dto.response.CookItemResponseDto;

public interface CookItemService {
    List<CookItemResponseDto> getCookItemsByCookId(Long cookId);
}
