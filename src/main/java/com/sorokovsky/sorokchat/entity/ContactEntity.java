package com.sorokovsky.sorokchat.entity;


import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "contacts", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_contacts_users",
                columnNames = {"first_user_id", "second_user_id"}
        )
})
@Entity
public class ContactEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity firstUser;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity secondUser;

    public static ContactEntity of(UserEntity first, UserEntity second) {
        if (first.getId().compareTo(second.getId()) <= 0) {
            return ContactEntity
                    .builder()
                    .firstUser(first)
                    .secondUser(second)
                    .build();
        } else {
            return ContactEntity
                    .builder()
                    .firstUser(second)
                    .secondUser(first)
                    .build();
        }
    }
}