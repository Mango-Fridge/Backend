package com.mango.mango.domain.user.dto.request;

// import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginDto {
    @NotBlank(message = "플랫폼은 필수 입력값입니다.")
    private String oauthProvider;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @Builder
    public UserLoginDto(String email, String oauthProvider) {
        this.email = email;
        this.oauthProvider = oauthProvider;
    }

}