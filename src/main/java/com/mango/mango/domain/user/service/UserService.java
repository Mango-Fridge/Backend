package com.mango.mango.domain.user.service;

import com.mango.mango.domain.user.dto.request.UserSignUpRequestDto;

public interface UserService {
    Long signUp(UserSignUpRequestDto requestDto);
    boolean isUsernameDuplicate(String nickname);
    boolean isEmailDuplicate(String email);
}