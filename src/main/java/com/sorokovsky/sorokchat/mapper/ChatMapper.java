package com.sorokovsky.sorokchat.mapper;

import com.sorokovsky.sorokchat.contract.GetChatPayload;
import com.sorokovsky.sorokchat.entity.ChatEntity;
import com.sorokovsky.sorokchat.model.ChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMapper {
    private final UserMapper userMapper;

    public ChatModel toModel(ChatEntity entity) {
        final var model = new ChatModel();
        model.setId(entity.getId());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setMembers(entity.getMembers().stream().map(userMapper::toModel).toList());
        return model;
    }

    public ChatEntity toEntity(ChatModel model) {
        final var entity = new ChatEntity();
        entity.setId(model.getId());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setMembers(model.getMembers().stream().map(userMapper::toEntity).toList());
        return entity;
    }

    public GetChatPayload toGet(ChatModel model) {
        return new GetChatPayload(
                model.getId(),
                model.getCreatedAt(),
                model.getUpdatedAt(),
                model.getName(),
                model.getDescription(),
                model.getMembers().stream().map(userMapper::toGet).toList()
        );
    }
}
