package com.mango.mango.domain.user.controller;

import com.mango.mango.domain.user.dto.request.UserLoginDto;
import com.mango.mango.domain.user.dto.response.UserLoginResponseDto;
import com.mango.mango.domain.user.dto.response.UserResponseDto;
import com.mango.mango.domain.user.service.UserService;
import com.mango.mango.global.error.CustomException;
import com.mango.mango.global.error.ErrorCode;
import com.mango.mango.global.jwt.JwtUtil;
import com.mango.mango.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "유저 관련 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final JwtUtil jwtUtil;


    /**
     * 카카로 로그인
     * @param authorizationHeader
     * @param userLoginDto
     * @param response
     * @return
     */
    @Operation(
        summary = "SNS 로그인",
        description = "프론트엔드에서 발급한 Access Token을 사용하여 로그인 처리.",
        security = { @SecurityRequirement(name = "BearerAuth") }
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponseDto>> login(
            @RequestHeader(name = "Authorization", required = true) String authorizationHeader,
            @RequestBody UserLoginDto userLoginDto, HttpServletResponse response) {
    
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
        }
    
        String accessToken = authorizationHeader.replace("Bearer ", "");
        UserLoginResponseDto userResponse = userService.login(userLoginDto, accessToken);

        String jwtToken = jwtUtil.createToken(userResponse.getUsrId());

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, jwtToken);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    /**
     * 단일 유저 정보 조회
     * @param userId
     * @return
     */
    @Operation(
        summary = "단일 유저 정보 조회",
        description = "단일 유저 정보에 대해서 불러온다.",
        security = { @SecurityRequirement(name = "BearerAuth") }
    )
    @GetMapping("/userInfo/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getInfoByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getInfoByUserId(userId)));
    }

    /**
     * 이메일 중복 검사
     * @param email
     * @return
     */
    @Operation(
        summary = "이메일 중복 검사",
        description = "유저 정보 중 겹치는 이메일이 있는지 체크한다"
    )
    @PostMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam String email) {
        boolean isDuplicate = userService.isEmailDuplicate(email);
        return ResponseEntity.ok(ApiResponse.success(isDuplicate));
    }


    /**
     * 회원 탈퇴
     * @param userId
     * @return
     */
    @Operation(
        summary = "회원 탈퇴",
        description = "계정 정보를 탈퇴한다. 동의 및 그룹 정보도 함께 제거 된다."
    )
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("회원 탈퇴가 완료되었습니다."));
    }
}