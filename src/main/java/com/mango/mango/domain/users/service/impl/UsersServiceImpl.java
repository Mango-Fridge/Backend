package com.mango.mango.domain.users.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mango.mango.domain.users.repository.UsersRepository;
import com.mango.mango.domain.users.service.UsersService;
import com.mango.mango.domain.users.dto.request.UserSignUpRequestDto;
import com.mango.mango.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;

// import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    // private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(UserSignUpRequestDto requestDto) {
        // 이메일 중복 검사
        if (usersRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 비밀번호 암호화
        // String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        
        // 회원 저장
        Users user = usersRepository.save(
            Users.builder()
                .email(requestDto.getEmail())
                // .password(encodedPassword)
                .password(requestDto.getPassword())
                .userName(requestDto.getUserName())
                .nickname(requestDto.getNickname())
                .phone(requestDto.getPhone())
                .build()
        );

        return user.getId();
    }
}