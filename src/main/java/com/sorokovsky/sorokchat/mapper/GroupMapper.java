package com.sorokovsky.sorokchat.mapper;

import com.sorokovsky.sorokchat.contract.GetGroupPayload;
import com.sorokovsky.sorokchat.entity.GroupEntity;
import com.sorokovsky.sorokchat.model.GroupModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GroupMapper {
    private final UserMapper userMapper;

    public GroupEntity toEntity(GroupModel model) {
        final var entity = GroupEntity
                .builder()
                .members(model.getMembers().stream().map(userMapper::toEntity).collect(Collectors.toSet()))
                .description(model.getDescription())
                .displayName(model.getDisplayName())
                .nickname(model.getNickname())
                .build();
        entity.setId(model.getId());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        return entity;
    }

    public GroupModel toModel(GroupEntity entity) {
        final var model = GroupModel
                .builder()
                .description(entity.getDescription())
                .displayName(entity.getDisplayName())
                .nickname(entity.getNickname())
                .members(entity.getMembers().stream().map(userMapper::toModel).collect(Collectors.toSet()))
                .build();
        model.setId(entity.getId());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    public GetGroupPayload toGet(GroupModel model) {
        return new GetGroupPayload(
                model.getId(),
                model.getCreatedAt(),
                model.getUpdatedAt(),
                model.getNickname(),
                model.getDisplayName(),
                model.getDescription(),
                model.getMembers().stream().map(userMapper::toGet).toList()
        );
    }
}
