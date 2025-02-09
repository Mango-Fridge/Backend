package com.mango.mango.domain.mail.service;

import com.mango.mango.domain.mail.dto.request.EmailVerifyRequestDto;

public interface EmailService {
    void sendVerificationEmail(String email);

    boolean confirmCode(EmailVerifyRequestDto requestDto);
}
