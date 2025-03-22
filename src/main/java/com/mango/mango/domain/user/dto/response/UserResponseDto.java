package com.mango.mango.domain.user.dto.response;

import com.mango.mango.domain.user.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponseDto {

    private int statusCode;
    private String email;
    private String usrNm;
    private Long usrId;
    private String oauthProvider;
    private boolean agreePrivacyPolicy;
    private boolean agreeTermsOfService;

    @Builder
    public UserResponseDto(int statusCode, String email, String usrNm, Long usrId, String oauthProvider,
                                boolean agreePrivacyPolicy, boolean agreeTermsOfService) {
        this.statusCode = statusCode;
        this.email = email;
        this.usrNm = usrNm;
        this.usrId = usrId;
        this.oauthProvider = oauthProvider;
        this.agreePrivacyPolicy = agreePrivacyPolicy;
        this.agreeTermsOfService = agreeTermsOfService;
    }

    public static UserResponseDto fromEntity(User user, boolean agreePrivacyPolicy, boolean agreeTermsOfService) {
        return UserResponseDto.builder()
            .email(user.getEmail())
            .usrNm(user.getUsername())
            .usrId(user.getId())
            .oauthProvider(user.getOauthProvider())
            .agreePrivacyPolicy(agreePrivacyPolicy)
            .agreeTermsOfService(agreeTermsOfService)
            .build();
    }
}
