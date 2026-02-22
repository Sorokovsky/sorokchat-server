package com.sorokovsky.sorokchat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "groups")
public class GroupEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String displayName;

    @Column
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "groups_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns =
            @JoinColumn(name = "group_id")
    )
    private Set<UserEntity> members;
}
