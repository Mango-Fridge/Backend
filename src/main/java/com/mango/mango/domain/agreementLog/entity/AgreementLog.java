package com.mango.mango.domain.agreementLog.entity;

import com.mango.mango.domain.users.entity.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "AGREEMENT_LOG")
public class AgreementLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOG_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private Users user;

    @Column(name = "KIND", nullable = false, length = 100)
    private String kind;

    @Column(name = "AGREED_AT")
    private LocalDateTime agreedAt;

    @Builder
    public AgreementLog(Users user, String kind) {
        this.user = user;
        this.kind = kind;
        this.agreedAt = LocalDateTime.now();
    }
}
