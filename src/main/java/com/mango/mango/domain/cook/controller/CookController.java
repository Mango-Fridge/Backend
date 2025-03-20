package com.mango.mango.domain.cook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mango.mango.domain.cook.dto.request.CookRequestDto;
import com.mango.mango.domain.cook.dto.response.CookSummaryResponseDto;
import com.mango.mango.domain.cook.service.CookService;
import com.mango.mango.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cooks")
@Tag(name = "Cook", description = "요리 관련 API")
public class CookController{

    @Autowired
    private CookService cookService;

    /**
     * 신규 요리 추가
     * @param cookRequestDto
     * @return
     */
    @Operation(
        summary = "그룹에 요리 추가",
        description = "그룹에 신규 요리를 추가한다."
    )
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> addNewCook(@Valid @RequestBody CookRequestDto cookRequestDto) throws Exception{
        return cookService.addNewCook(cookRequestDto);
    }

    /**
     * 요리 리스트 호출
     * @param cookRequestDto
     * @return
     */
    @Operation(
        summary = "요리 리스트 불러오기",
        description = "특정 그룹 요리 리스트 요약을 불러온다"
    )
    @GetMapping("/list/{groupId}")
    public ResponseEntity<ApiResponse<List<CookSummaryResponseDto>>> getCookList(@Valid @PathVariable Long groupId) throws Exception{
        return cookService.getCookList(groupId);
    }
}
