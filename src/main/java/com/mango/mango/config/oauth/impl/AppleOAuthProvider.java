package com.mango.mango.config.oauth.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mango.mango.config.oauth.OAuthProvider;
import com.mango.mango.domain.user.entity.User;
import com.mango.mango.global.error.CustomException;
import com.mango.mango.global.error.ErrorCode;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class AppleOAuthProvider implements OAuthProvider {

    private static final String APPLE_USER_INFO_URL = "https://appleid.apple.com/auth/token";
    
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public AppleOAuthProvider(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public User getUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                APPLE_USER_INFO_URL, HttpMethod.POST, entity, String.class
            );

            if (response.getBody() == null) {
                throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
            }

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String appleUserId = jsonNode.get("sub").asText();
            String email = jsonNode.has("email") ? jsonNode.get("email").asText() : null;

            if (appleUserId == null) {
                throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
            }

            return User.builder()
                    .email(email != null ? email : appleUserId + "@apple.com")
                    .username("MangoUser")
                    .oauthProvider("APPLE")
                    .build();

        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
        }
    }
}