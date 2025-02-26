package com.mango.mango.domain.agreementLog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mango.mango.domain.agreementLog.entity.AgreementLog;

@Repository
public interface AgreementLogRepository extends JpaRepository<AgreementLog, Long> {
}