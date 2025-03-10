package com.mango.mango.domain.agreementLog.service;

import com.mango.mango.domain.agreementLog.dto.request.AgreementRequestDto;
import com.mango.mango.domain.agreementLog.dto.response.AgreementResponseDto;

public interface AgreementLogService {

    public AgreementResponseDto agree(AgreementRequestDto requestDto);
}
