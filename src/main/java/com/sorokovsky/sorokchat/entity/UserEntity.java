package com.sorokovsky.sorokchat.entity;

import com.sorokovsky.sorokchat.model.Authority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String middleName;

    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @ElementCollection(targetClass = Authority.class)
    @Column
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_contacts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id"),
            uniqueConstraints = @UniqueConstraint(
                    name = "uk_user_contact",
                    columnNames = {"user_id", "contact_id"}
            )
    )
    private Set<UserEntity> contacts = new HashSet<>();
}
