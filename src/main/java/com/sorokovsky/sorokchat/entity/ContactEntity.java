package com.sorokovsky.sorokchat.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "contacts")
public class ContactEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity firstUser;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity secondUser;
}