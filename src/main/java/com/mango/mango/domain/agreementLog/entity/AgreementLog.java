package com.mango.mango.domain.agreementLog.entity;

import com.mango.mango.domain.base.entity.BaseEntity;
import com.mango.mango.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "AGREEMENT_LOG")
public class AgreementLog extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOG_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "KIND", nullable = false, length = 100)
    private String kind;

    @Column(name = "AGREE_YN")
    private boolean agreeYn;

    @Builder
    public AgreementLog(User user, String kind, Boolean agreeYn) {
        this.user = user;
        this.kind = kind;
        this.agreeYn = agreeYn;
    }

    // 동의 상태를 업데이트하는 메서드
    public void updateAgreement(boolean agreeYn) {
        this.agreeYn = agreeYn;
    }
}