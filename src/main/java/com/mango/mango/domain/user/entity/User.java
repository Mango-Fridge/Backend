package com.mango.mango.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import com.mango.mango.domain.agreementLog.entity.AgreementLog;
import com.mango.mango.domain.base.entity.BaseEntity;

import jakarta.persistence.CascadeType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
public class User extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USR_ID")
    private Long id;

    @Column(name = "USR_NM", nullable = false, length = 50)
    private String username;

    @Column(name = "EMAIL", nullable = false, length = 50)
    private String email;

    @Column(name = "OAUTH_PROVIDER", nullable = false, length = 30)
    private String oauthProvider;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AgreementLog> agreementLogs = new ArrayList<>();

    @Builder
    public User(String username, String email, String oauthProvider) {
        this.username = username;
        this.email = email;
        this.oauthProvider = oauthProvider;
    }
}