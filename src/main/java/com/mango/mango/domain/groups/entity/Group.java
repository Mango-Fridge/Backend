package com.mango.mango.domain.groups.entity;

import com.mango.mango.domain.base.entity.BaseEntity;
import com.mango.mango.domain.contents.entity.Content;
import com.mango.mango.domain.groupMembers.entity.GroupMember;
import com.mango.mango.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_groups")
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
    private Set<GroupMember> groupMembers = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> contents = new ArrayList<>();

    @Builder
    public Group(String groupName, User groupOwner, Content content) {
        this.groupName = groupName;
        this.groupOwner = groupOwner;
    }

    public Group(Long groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }
}