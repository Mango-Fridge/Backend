package com.mango.mango.domain.agreementLog.constant;

public enum AgreementType {
    PRIVACY_POLICY("개인정보 수집이용 동의"),
    TERMS_OF_SERVICE("서비스 이용약관 동의");

    private final String description;

    AgreementType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}