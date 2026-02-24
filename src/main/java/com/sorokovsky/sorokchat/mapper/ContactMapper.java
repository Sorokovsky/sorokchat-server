package com.sorokovsky.sorokchat.mapper;

import com.sorokovsky.sorokchat.contract.GetContactPayload;
import com.sorokovsky.sorokchat.entity.ContactEntity;
import com.sorokovsky.sorokchat.model.ContactModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContactMapper {
    private final UserMapper userMapper;

    public ContactEntity toEntity(ContactModel model) {
        final var entity = ContactEntity
                .builder()
                .firstUser(userMapper.toEntity(model.getFirstUser()))
                .secondUser(userMapper.toEntity(model.getSecondUser()))
                .build();
        entity.setId(model.getId());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        return entity;
    }

    public ContactModel toModel(ContactEntity entity) {
        final var model = ContactModel
                .builder()
                .firstUser(userMapper.toModel(entity.getFirstUser()))
                .secondUser(userMapper.toModel(entity.getSecondUser()))
                .build();
        model.setId(entity.getId());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    public GetContactPayload toGet(ContactModel model) {
        return new GetContactPayload(
                model.getId(),
                model.getCreatedAt(),
                model.getUpdatedAt(),
                userMapper.toGet(model.getFirstUser()),
                userMapper.toGet(model.getSecondUser())
        );
    }
}
