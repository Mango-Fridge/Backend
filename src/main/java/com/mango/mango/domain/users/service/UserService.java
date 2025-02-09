package com.mango.mango.domain.users.service;

import com.mango.mango.domain.users.dto.request.UserSignUpRequestDto;

public interface UserService {
    Long signUp(UserSignUpRequestDto requestDto);
}
