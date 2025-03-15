package com.mango.mango.domain.groups.entity;

import com.mango.mango.domain.base.entity.BaseEntity;
import com.mango.mango.domain.user.entity.User;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "group_user")
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
}