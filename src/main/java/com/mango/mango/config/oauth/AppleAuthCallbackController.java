package com.mango.mango.config.oauth;

import com.mango.mango.config.oauth.dto.response.AppleTokenResponse;
import com.mango.mango.config.oauth.impl.AppleOAuthService;
import com.mango.mango.domain.user.dto.request.UserLoginDto;
import com.mango.mango.domain.user.dto.response.UserLoginResponseDto;
import com.mango.mango.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/apple")
@RequiredArgsConstructor
public class AppleAuthCallbackController {

    private final AppleOAuthService appleOAuthService;
    
    private final UserService userService;
    
    @PostMapping("/callback")
    public ResponseEntity<?> handleAppleCallbackPost(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String id_token,
            @RequestParam(required = false) String state
    ) {
        return loginFlow(code, id_token, state);
    }
    
    @GetMapping("/callback")
    public ResponseEntity<?> handleAppleCallbackGet(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String id_token,
            @RequestParam(required = false) String state
    ) {
        return loginFlow(code, id_token, state);
    }
    
    private ResponseEntity<?> loginFlow(String code, String id_token, String state) {
        try {
            AppleTokenResponse tokenResponse = appleOAuthService.getAppleToken(code);
    
            UserLoginDto loginDto = UserLoginDto.builder()
                    .oauthProvider("APPLE")
                    .build();
    
            UserLoginResponseDto loginResponse = userService.login(loginDto, tokenResponse.getIdToken());
    
            return ResponseEntity.ok(loginResponse);
    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Apple 로그인 실패: " + e.getMessage());
        }
    }
}