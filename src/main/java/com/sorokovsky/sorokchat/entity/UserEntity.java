package com.sorokovsky.sorokchat.entity;

import com.sorokovsky.sorokchat.model.Authority;
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
@Entity(name = "users")
public class UserEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String password;

    @Column()
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @ElementCollection(targetClass = Authority.class)
    @Column
    private Set<Authority> authorities;
}
