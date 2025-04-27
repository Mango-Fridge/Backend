package com.mango.mango.config.oauth.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mango.mango.config.oauth.dto.response.AppleTokenResponse;
import com.mango.mango.global.error.CustomException;
import com.mango.mango.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppleOAuthService {

    private static final String APPLE_TOKEN_URL = "https://appleid.apple.com/auth/token";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AppleJwtGenerator appleJwtGenerator;

    @Value("${OAUTH_APPLE_CLIENT_ID}")
    private String clientId;

    public AppleTokenResponse getAppleToken(String authorizationCode) {
        try {
            String clientSecret = appleJwtGenerator.generateClientSecret();
    
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
            Map<String, String> bodyMap = new LinkedHashMap<>();
            bodyMap.put("client_id", clientId);
            bodyMap.put("client_secret", clientSecret);
            bodyMap.put("code", authorizationCode);
            bodyMap.put("grant_type", "authorization_code");
            bodyMap.put("redirect_uri", "https://295a-121-136-141-135.ngrok-free.app/auth/apple/callback");
    
            String formData = buildFormData(bodyMap);
    
            HttpEntity<String> entity = new HttpEntity<>(formData, headers);
    
            ResponseEntity<String> response = restTemplate.exchange(
                    APPLE_TOKEN_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
    
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
            }
    
            return objectMapper.readValue(response.getBody(), AppleTokenResponse.class);
    
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
        }
    }

    public String buildFormData(Map<String, String> params) {
    StringBuilder builder = new StringBuilder();
    for (Map.Entry<String, String> entry : params.entrySet()) {
        if (builder.length() > 0) {
            builder.append("&");
        }
        builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
        builder.append("=");
        builder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
    }
    return builder.toString();
}
}