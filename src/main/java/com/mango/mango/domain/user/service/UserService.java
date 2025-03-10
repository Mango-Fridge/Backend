package com.mango.mango.domain.user.service;

import com.mango.mango.domain.user.dto.request.UserLoginDto;
import com.mango.mango.domain.user.dto.request.UserSignUpRequestDto;
import com.mango.mango.domain.user.dto.response.UserLoginResponseDto;

public interface UserService {
    Long signUp(UserSignUpRequestDto requestDto);
    UserLoginResponseDto login(UserLoginDto requestDto, String accessToken);
    // boolean isPhoneDuplicate(String phone);
    // boolean isNicknameDuplicate(String nickname);
    boolean isEmailDuplicate(String email);
}