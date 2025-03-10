package com.mango.mango.domain.user.dto.request;

import com.mango.mango.domain.user.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignUpRequestDto {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String username;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 8자 이상, 문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @NotNull(message = "서비스 이용약관 동의는 필수입니다.")
    private Boolean serviceAgreement;

    @NotNull(message = "개인정보 처리방침 동의는 필수입니다.")
    private Boolean privacyAgreement;

    @Builder
    public UserSignUpRequestDto(String username, String email, String password,
                              Boolean serviceAgreement, Boolean privacyAgreement) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.serviceAgreement = serviceAgreement;
        this.privacyAgreement = privacyAgreement;
    }

    public User toEntity(String encodedPassword) {
        return User.builder()
                .username(username)
                .email(email)
                .build();
    }
}