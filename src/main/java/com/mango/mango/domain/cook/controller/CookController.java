package com.mango.mango.domain.cook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mango.mango.domain.cook.dto.request.CookRequestDto;
import com.mango.mango.domain.cook.dto.request.CookUpdateRequestDto;
import com.mango.mango.domain.cook.dto.response.CookDetailResponseDto;
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
        summary = "요리 - 요리 추가",
        description = "그룹에 신규 요리를 추가한다."
    )
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> addNewCook(@Valid @RequestBody CookRequestDto request) throws Exception{
        return cookService.addNewCook(request);
    }

    /**
     * 요리 리스트 호출
     * @param cookRequestDto
     * @return
     */
    @Operation(
        summary = "요리 - 요리 리스트",
        description = "특정 그룹 요리 리스트 요약을 불러온다"
    )
    @GetMapping("/list/{groupId}")
    public ResponseEntity<ApiResponse<List<CookSummaryResponseDto>>> getCookList(@Valid @PathVariable Long groupId) throws Exception{
        return cookService.getCookList(groupId);
    }

    /**
     * 요리 상세 정보
     * @param cookId
     * @return
     * @throws Exception
     */
    @Operation(
        summary = "요리 - 요리 상세 정보",
        description = "특정 요리의 상세 정보 전체를 불러온다"
    )
    @GetMapping("/{cookId}")
    public ResponseEntity<ApiResponse<CookDetailResponseDto>> getCookDetail(@Valid @PathVariable Long cookId) throws Exception{
        return cookService.getCookDetail(cookId);
    }

    /**
     * 요리 정보 수정 (아이템 X)
     * @param cookId
     * @param cookUpdateRequestDto
     * @return
     * @throws Exception
     */
    @Operation(
        summary = "요리 - 요리 수정",
        description = "특정 요리의 상세 정보를 수정한다"
    )
    @PutMapping("/{cookId}")
    public ResponseEntity<ApiResponse<Void>> updateCookDetail(
        @Valid @PathVariable Long cookId,
        @RequestBody CookUpdateRequestDto request) throws Exception{
        return cookService.updateCookDetail(cookId, request);
    }

    /**
     * 요리 삭제
     * @param cookId
     * @return
     * @throws Exception
     */
    @Operation(
        summary = "요리 - 요리 삭제",
        description = "요리를 삭제한다"
    )
    @DeleteMapping("/{cookId}")
    public ResponseEntity<ApiResponse<Void>> deleteCook(
        @Valid @PathVariable Long cookId
    ) throws Exception{
        return cookService.deleteCook(cookId);
    }
}