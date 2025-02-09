package com.mango.mango.domain.mail.service;

public interface EmailService {
    void sendVerificationEmail(String email, String code);
}
