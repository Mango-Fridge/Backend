package com.mango.mango.domain.groupUsers.entity;

import com.mango.mango.domain.base.entity.BaseEntity;
import com.mango.mango.domain.groups.entity.Group;
import com.mango.mango.domain.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_users")
public class GroupUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GRP_ID", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "USR_ID", nullable = false)
    private User user;

    @Builder
    public GroupUser(Group group, User user) {
        this.group = group;
        this.user = user;
    }
}