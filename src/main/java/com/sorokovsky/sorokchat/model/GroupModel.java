package com.sorokovsky.sorokchat.model;

import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupModel extends BaseModel {
    private String nickname;
    private String displayName;
    private String description;
    private Set<UserModel> members;
}
