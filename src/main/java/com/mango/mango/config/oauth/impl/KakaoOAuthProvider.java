package com.mango.mango.config.oauth.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mango.mango.config.oauth.OAuthProvider;
import com.mango.mango.domain.user.entity.User;
import com.mango.mango.global.error.CustomException;
import com.mango.mango.global.error.ErrorCode;

import java.util.HashMap;
import java.util.Map;

@Component
@SuppressWarnings("rawtypes")
public class KakaoOAuthProvider implements OAuthProvider {

    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    // https://kauth.kakao.com/oauth/authorize?client_id=4985bb6e1259e1eea63d88a7decc596b&redirect_uri=http://localhost:8080/auth/kakao-login&response_type=code
// 3qqrRHAvfu9BpIBox7B5FiKUocD3qME9Mz_Qsau1Abhe9X2rg7pajAAAAAQKPXObAAABlXU1qSyBPKUF0hG4dQ

// odh4311@naver.com
// 1218605a!

    @Value("${OAUTH_KAKAO_CLIENT_ID}")
    private String kakaoClientId;

    @Value("${OAUTH_KAKAO_REDIRECT_URI}")
    private String kakaoRedirectUri;

    /**
     * 카카오 Access Token 발급 메서드
     */
    public String getAccessToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "authorization_code");
        requestBody.put("client_id", kakaoClientId);
        requestBody.put("redirect_uri", kakaoRedirectUri);
        requestBody.put("code", authorizationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(KAKAO_TOKEN_URL, requestEntity, Map.class);

            if (response.getBody() == null || !response.getBody().containsKey("access_token")) {
                throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
            }

            return (String) response.getBody().get("access_token");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
        }
    }

    /**
     * 카카오 사용자 정보 가져오기
     */
    public User getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
    
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
    
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                KAKAO_USER_INFO_URL, HttpMethod.GET, entity, Map.class
            );
    
            if (response.getBody() == null || !response.getBody().containsKey("id")) {
                throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
            }
    
            @SuppressWarnings("unchecked")
            Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
            String email = (String) kakaoAccount.get("email");
            @SuppressWarnings("unchecked")
            String nickname = (String) ((Map<String, Object>) kakaoAccount.get("profile")).get("nickname");
    
            return User.builder()
                .email(email)
                .username(nickname)
                .oauthProvider("KAKAO")
                .build();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
        }
    }
}