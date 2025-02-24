package com.mango.mango.domain.users.service;

import com.mango.mango.domain.users.dto.request.UserSignUpRequestDto;

public interface UserService {
    Long signUp(UserSignUpRequestDto requestDto);
    boolean isPhoneDuplicate(String phone);
    boolean isNicknameDuplicate(String nickname);
    boolean isEmailDuplicate(String email);
}