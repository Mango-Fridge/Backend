package com.mango.mango.domain.mail.service.serviceImpl;

import com.mango.mango.domain.mail.dto.request.EmailVerifyRequestDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.mango.mango.domain.mail.service.EmailService;

import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void sendVerificationEmail(String email) {
        // 인증번호 생성 (6자리)
        String verificationCode = generateVerificationCode();
        String key = "EmailVerification:" + email;

        // 기존 이메일로 인증 번호를 보낸 경우 기존 인증코드 삭제
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.delete(key);
        }

        // Redis에 인증번호 저장 (만료시간 2분)
        redisTemplate.opsForValue().set(key, verificationCode, Duration.ofMinutes(2));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mango 이메일 인증 코드 발송");
        message.setText("인증 코드: " + verificationCode);
        javaMailSender.send(message);
    }

    @Override
    public boolean confirmCode(EmailVerifyRequestDto requestDto) {
        String savedCode = redisTemplate.opsForValue()
                .get("EmailVerification:" + requestDto.getEmail());

        boolean isValid = requestDto.getCode().equals(savedCode);

        // 인증 성공 시 Redis에서 해당 인증 코드 삭제
        if (isValid) {
            redisTemplate.delete("EmailVerification:" + requestDto.getEmail());
        }

        return isValid;
    }

    // 6자리 난수 생성 메소드
    private String generateVerificationCode() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}