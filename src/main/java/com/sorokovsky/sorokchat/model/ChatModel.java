package com.sorokovsky.sorokchat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatModel extends BaseModel {
    private String name;
    private String description;
    private List<UserModel> members;
}
