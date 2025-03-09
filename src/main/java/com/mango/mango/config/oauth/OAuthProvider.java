package com.mango.mango.config.oauth;

import com.mango.mango.domain.user.entity.User;

public interface OAuthProvider {
    User getUserInfo(String accessToken);
}