package com.mango.mango.domain.user.entity;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mango.mango.domain.agreementLog.entity.AgreementLog;
import com.mango.mango.domain.base.entity.BaseEntity;
import com.mango.mango.domain.groups.entity.Group;
import com.mango.mango.domain.groupUsers.entity.GroupUser;


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

    // 사용자가 그룹장으로 있는 그룹 목록 (1:N)
    @OneToMany(mappedBy = "groupOwner")
    private Set<Group> ownedGroups = new HashSet<>();

    // 사용자가 가입한 그룹 목록 (중간 엔티티를 통해 매핑)
    @OneToMany(mappedBy = "user")
    private Set<GroupUser> groupMembers = new HashSet<>();

    @Builder
    public User(String username, String email, String oauthProvider) {
        this.username = username;
        this.email = email;
        this.oauthProvider = oauthProvider;
    }

    public void updateUsername(String newUsername){
        this.username = newUsername;
    }
}