package com.mango.mango.domain.agreementLog.service.impl;

import org.springframework.stereotype.Service;
import com.mango.mango.domain.agreementLog.constant.AgreementType;
import com.mango.mango.domain.agreementLog.dto.request.AgreementRequestDto;
import com.mango.mango.domain.agreementLog.dto.response.AgreementResponseDto;
import com.mango.mango.domain.agreementLog.entity.AgreementLog;
import com.mango.mango.domain.agreementLog.repository.AgreementLogRepository;
import com.mango.mango.domain.agreementLog.service.AgreementLogService;
import com.mango.mango.domain.user.entity.User;
import com.mango.mango.domain.user.repository.UserRepository;
import com.mango.mango.global.error.CustomException;
import com.mango.mango.global.error.ErrorCode;
import jakarta.transaction.Transactional;

@Service
public class AgreementLogServiceImpl implements AgreementLogService {

    private final AgreementLogRepository agreementLogRepository;
    private final UserRepository userRepository;

    public AgreementLogServiceImpl(AgreementLogRepository agreementLogRepository, UserRepository userRepository) {
        this.agreementLogRepository = agreementLogRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AgreementResponseDto agree(AgreementRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUsrId())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        AgreementLog privacyPolicyLog = agreementLogRepository.findByUserAndKind(user, AgreementType.PRIVACY_POLICY.name())
            .orElseGet(() -> new AgreementLog(user, AgreementType.PRIVACY_POLICY.name(), requestDto.isAgreePrivacyPolicy())); 

        privacyPolicyLog.updateAgreement(requestDto.isAgreePrivacyPolicy());
        agreementLogRepository.save(privacyPolicyLog);

        AgreementLog termsOfServiceLog = agreementLogRepository.findByUserAndKind(user, AgreementType.TERMS_OF_SERVICE.name())
            .orElseGet(() -> new AgreementLog(user, AgreementType.TERMS_OF_SERVICE.name(), requestDto.isAgreeTermsOfService()));

        termsOfServiceLog.updateAgreement(requestDto.isAgreeTermsOfService());
        agreementLogRepository.save(termsOfServiceLog);

        // 응답 반환
        return new AgreementResponseDto(200);
    }
}