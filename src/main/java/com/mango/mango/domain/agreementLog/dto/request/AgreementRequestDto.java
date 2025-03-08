package com.mango.mango.domain.agreementLog.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AgreementRequestDto {
    private Long usrId;
    private boolean agreePrivacyPolicy;
    private boolean agreeTermsOfService;
}