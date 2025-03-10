package com.mango.mango.domain.agreementLog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mango.mango.domain.agreementLog.entity.AgreementLog;
import com.mango.mango.domain.user.entity.User;

@Repository
public interface AgreementLogRepository extends JpaRepository<AgreementLog, Long> {

    boolean existsByUserAndKindAndAgreeYn(User user, String name, boolean b);

    Optional<AgreementLog> findByUserAndKind(User user, String name);
}