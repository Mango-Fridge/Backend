package com.mango.mango.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserLoginResponseDto {
    private int statusCode;
    private String email;
    private String usrNm;
    private Long usrId;
    private String oauthProvider;
    private boolean agreePrivacyPolicy;
    private boolean agreeTermsOfService;
    private boolean isNewUser;

    @Builder
    public UserLoginResponseDto(int statusCode, String email, String usrNm, Long usrId, String oauthProvider,
                                boolean agreePrivacyPolicy, boolean agreeTermsOfService, boolean isNewUser) {
        this.statusCode = statusCode;
        this.email = email;
        this.usrNm = usrNm;
        this.usrId = usrId;
        this.oauthProvider = oauthProvider;
        this.agreePrivacyPolicy = agreePrivacyPolicy;
        this.agreeTermsOfService = agreeTermsOfService;
        this.isNewUser = isNewUser;
    }
}