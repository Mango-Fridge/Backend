package com.mango.mango.domain.groups.entity;

import com.mango.mango.domain.base.entity.BaseEntity;
import com.mango.mango.domain.contents.entity.Content;
import com.mango.mango.domain.user.entity.User;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "`groups`")
public class Group extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GRP_ID")
    private Long groupId;

    @Column(name = "GRP_NM", length = 100, nullable = false)
    private String groupName;

    // 그룹장 (N:1 관계)
    @ManyToOne
    @JoinColumn(name = "GRP_OWN_ID", nullable = false)
    private User groupOwner;

    // 그룹의 멤버들 (중간 엔티티를 통해 매핑)
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GroupUser> groupMembers = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "CONTENT_ID", nullable = false, unique = true)
    private Content content;
}