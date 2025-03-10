package com.mango.mango.domain.user.controller;

import com.mango.mango.domain.user.dto.request.UserLoginDto;
import com.mango.mango.domain.user.dto.response.UserLoginResponseDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "유저 관련 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final JwtUtil jwtUtil;


    /**
     * POST /users/login HTTP/1.1
     *  Host: api.example.com
     *  Authorization: Bearer your-access-token
     *  Content-Type: application/json
     *
     *  {
     *      "oauthProvider": "KAKAO"
     *  }
     * @param authorizationHeader
     * @param userLoginDto
     * @param response
     * @return
     */
    @Operation(
        summary = "카카오 로그인",
        description = "프론트엔드에서 발급한 Access Token을 사용하여 로그인 처리.",
        security = { @SecurityRequirement(name = "BearerAuth") }
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponseDto>> login(
            @RequestHeader(name = "Authorization", required = true) String authorizationHeader,
            @RequestBody UserLoginDto userLoginDto, HttpServletResponse response) {
    
        System.out.println("Received Authorization Header: " + authorizationHeader);
    
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

    // 회원가입
    // @PostMapping("/signup")
    // public ResponseEntity<ApiResponse<Long>> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
    //     Long userId = userService.signUp(requestDto);
    //     return ResponseEntity.ok(ApiResponse.success(userId));
    // }

    // 이메일 중복 유효성 검사 ( 존재: true, 존재하지 않음: false )
    @PostMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam String email) {
        boolean isDuplicate = userService.isEmailDuplicate(email);
        return ResponseEntity.ok(ApiResponse.success(isDuplicate));
    }
}