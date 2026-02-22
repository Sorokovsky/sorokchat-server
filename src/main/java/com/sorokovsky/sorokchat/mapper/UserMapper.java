package com.sorokovsky.sorokchat.mapper;

import com.sorokovsky.sorokchat.entity.UserEntity;
import com.sorokovsky.sorokchat.model.Authority;
import com.sorokovsky.sorokchat.model.UserModel;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserModel toModel(UserEntity entity) {
        final var model = UserModel
                .builder()
                .email(entity.getEmail())
                .password(entity.getPassword())
                .displayName(entity.getDisplayName())
                .phoneNumber(entity.getPhoneNumber())
                .authorities(entity.getAuthorities()
                        .stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.name()))
                        .collect(Collectors.toSet()))
                .nickname(entity.getNickname())
                .build();
        model.setId(entity.getId());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setCreatedAt(entity.getCreatedAt());
        return model;
    }

    public UserEntity toEntity(UserModel model) {
        final var entity = UserEntity
                .builder()
                .email(model.getEmail())
                .password(model.getPassword())
                .displayName(model.getDisplayName())
                .phoneNumber(model.getPhoneNumber())
                .authorities(model.getAuthorities()
                        .stream()
                        .map(authority -> Authority.valueOf(authority.getAuthority()))
                        .collect(Collectors.toSet()))
                .nickname(model.getNickname())
                .build();
        entity.setId(model.getId());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setCreatedAt(model.getCreatedAt());
        return entity;
    }
}
