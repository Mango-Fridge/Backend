package com.mango.mango.domain.user.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mango.mango.domain.user.repository.UserRepository;
import com.mango.mango.domain.user.service.UserService;
import com.mango.mango.global.error.ErrorCode;
import com.mango.mango.domain.user.dto.request.UserSignUpRequestDto;
import com.mango.mango.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import com.mango.mango.global.error.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository usersRepository;

    @Transactional
    public Long signUp(UserSignUpRequestDto requestDto) {

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";

        // 이메일 유효성 체크
        if (!requestDto.getEmail().matches(emailRegex)) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);
        }

        // 필수 약관 동의 체크
        if(!requestDto.getPrivacyAgreement().equals(true) ||
                !requestDto.getServiceAgreement().equals(true)){
            throw new CustomException(ErrorCode.REQUIRED_AGREEMENT);
        }

        // 이메일 중복 검사
        if (usersRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 닉네임 중복 검사
        if (usersRepository.existsByUsername(requestDto.getUsername())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // 회원 저장
        User user = usersRepository.save(
            User.builder()
                .email(requestDto.getEmail())
                .username(requestDto.getUsername())
                .build()
        );

        return user.getId();
    }

    @Override
    public boolean isUsernameDuplicate(String nickname) {
        return usersRepository.existsByUsername(nickname);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return usersRepository.existsByEmail(email);
    }
}