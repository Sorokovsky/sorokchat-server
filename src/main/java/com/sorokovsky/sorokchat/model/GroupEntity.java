package com.sorokovsky.sorokchat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupEntity extends BaseModel {
    private String nickname;
    private String displayName;
    private String description;
    private Set<UserModel> members;
}
