package com.mango.mango.domain.users.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mango.mango.domain.users.repository.UserRepository;
import com.mango.mango.domain.users.service.UserService;
import com.mango.mango.global.error.ErrorCode;
import com.mango.mango.domain.users.dto.request.UserSignUpRequestDto;
import com.mango.mango.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import com.mango.mango.global.error.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository usersRepository;
    // private final PasswordEncoder passwordEncoder;

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

        // 전화번호 중복 검사
        if (usersRepository.existsByPhone(requestDto.getPhone())) {
            throw new CustomException(ErrorCode.DUPLICATE_PHONE_NUMBER);
        }

        // 닉네임 중복 검사
        if (usersRepository.existsByNickname(requestDto.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // 비밀번호 암호화
        // String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        
        // 회원 저장
        Users user = usersRepository.save(
            Users.builder()
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .username(requestDto.getUsername())
                .nickname(requestDto.getNickname())
                .phone(requestDto.getPhone())
                .build()
        );

        return user.getId();
    }

    @Override
    public boolean isPhoneDuplicate(String phone) {
        return usersRepository.existsByPhone(phone);
    }

    @Override
    public boolean isNicknameDuplicate(String nickname) {
        return usersRepository.existsByNickname(nickname);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return usersRepository.existsByEmail(email);
    }
}