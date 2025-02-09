package com.mango.mango.domain.mail.dto.request;

import lombok.Getter;

@Getter
public class EmailVerifyRequestDto {
    private String email;
    private String code;
}
