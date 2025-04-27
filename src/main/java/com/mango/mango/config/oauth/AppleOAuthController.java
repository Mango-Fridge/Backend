package com.mango.mango.config.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.mango.mango.config.oauth.dto.response.AppleTokenResponse;
import com.mango.mango.config.oauth.impl.AppleJwtGenerator;
import com.mango.mango.config.oauth.impl.AppleOAuthService;

@RestController
@RequestMapping("/auth/apple")
@RequiredArgsConstructor
public class AppleOAuthController {

    private final AppleOAuthService appleOAuthService;
    private final AppleJwtGenerator appleJwtGenerator;

    @PostMapping("/token")
    public AppleTokenResponse exchangeAuthorizationCode(@RequestParam String code) {
        return appleOAuthService.getAppleToken(code);
    }

    @GetMapping("/test/jwt")
    public String testGenerateJwt() throws Exception {
        String jwt = appleJwtGenerator.generateClientSecret();
        System.out.println("Generated JWT: " + jwt);
        return jwt;
    }
}