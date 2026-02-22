package com.sorokovsky.sorokchat.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "contacts")
public class ContactEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity firstUser;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity secondUser;
}