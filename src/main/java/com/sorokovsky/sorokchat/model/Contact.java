package com.sorokovsky.sorokchat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
public class Contact extends BaseModel {
    private final UserModel firstUser;
    private final UserModel secondUser;
}