package com.mango.mango.config.oauth.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mango.mango.config.oauth.OAuthProvider;
import com.mango.mango.domain.user.entity.User;
import com.mango.mango.global.error.CustomException;
import com.mango.mango.global.error.ErrorCode;
import org.springframework.stereotype.Component;


@Component
public class AppleOAuthProvider implements OAuthProvider {

    @Override
    public User getUserInfo(String idToken) {
        try {
            DecodedJWT decodedJWT = JWT.decode(idToken);
            String appleUserId = decodedJWT.getSubject();
            String email = decodedJWT.getClaim("email").asString();

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