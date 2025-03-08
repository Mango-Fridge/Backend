package com.mango.mango.domain.agreementLog.entity;

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
    private User user;

    @Column(name = "KIND", nullable = false, length = 100)
    private String kind;

    @Column(name = "AGREEYN")
    private boolean agreeYn;

    @Column(name = "AGREED_AT")
    private LocalDateTime agreedAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Builder
    public AgreementLog(User user, String kind) {
        this.user = user;
        this.kind = kind;
        this.agreedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        agreeYn = false;
    }
}