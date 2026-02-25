package com.sorokovsky.sorokchat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
public class ContactModel extends BaseModel {
    private final UserModel firstUser;
    private final UserModel secondUser;

    public boolean hasUser(UserModel user) {
        return firstUser.getId().equals(user.getId()) || secondUser.getId().equals(user.getId());
    }
}