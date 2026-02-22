package com.sorokovsky.sorokchat.mapper;

import com.sorokovsky.sorokchat.entity.UserEntity;
import com.sorokovsky.sorokchat.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserModel toModel(UserEntity entity) {
        final var model = new UserModel(
                entity.getNickname(),
                entity.getDisplayName(),
                entity.getPassword(),
                entity.getPhoneNumber(),
                entity.getEmail(),
                entity.getAuthorities()
        );
        model.setId(entity.getId());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setCreatedAt(entity.getCreatedAt());
        return model;
    }

    public UserEntity toEntity(UserModel model) {
        final var entity = new UserEntity(
                model.getNickname(),
                model.getDisplayName(),
                model.getPassword(),
                model.getPhoneNumber(),
                model.getEmail(),
                model.getAuthorities()
        );
        entity.setId(model.getId());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setCreatedAt(model.getCreatedAt());
        return entity;
    }
}
