package com.mango.mango.domain.user.entity;

import com.mango.mango.domain.groups.entity.GroupUser;
import com.mango.mango.domain.groups.entity.Group;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mango.mango.domain.agreementLog.entity.AgreementLog;
import com.mango.mango.domain.agreementLog.constant.AgreementType;
import jakarta.persistence.CascadeType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USR_ID")
    private Long id;

    @Column(name = "USR_NM", nullable = false, length = 50)
    private String username;

    @Column(name = "EMAIL", nullable = false, length = 50)
    private String email;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AgreementLog> agreementLogs = new ArrayList<>();

    // 사용자가 그룹장으로 있는 그룹 목록 (1:N)
    @OneToMany(mappedBy = "groupOwner")
    private Set<Group> ownedGroups = new HashSet<>();

    // 사용자가 가입한 그룹 목록 (중간 엔티티를 통해 매핑)
    @OneToMany(mappedBy = "user")
    private Set<GroupUser> groupMembers = new HashSet<>();

    @Builder
    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.agreementLogs.add(new AgreementLog(this, AgreementType.PRIVACY_POLICY.name()));
        this.agreementLogs.add(new AgreementLog(this, AgreementType.TERMS_OF_SERVICE.name()));
    }
}