package com.mango.mango.domain.user.service;

import com.mango.mango.domain.user.dto.request.UserLoginDto;
import com.mango.mango.domain.user.dto.request.UserSignUpRequestDto;
import com.mango.mango.domain.user.dto.response.UserLoginResponseDto;
import com.mango.mango.domain.user.dto.response.UserResponseDto;

public interface UserService {
    Long signUp(UserSignUpRequestDto requestDto);
    UserResponseDto getInfoByUserId(Long userId);
    UserLoginResponseDto login(UserLoginDto requestDto, String accessToken);
    // boolean isPhoneDuplicate(String phone);
    // boolean isNicknameDuplicate(String nickname);
    boolean isEmailDuplicate(String email);
    void deleteUser(Long userId);
}