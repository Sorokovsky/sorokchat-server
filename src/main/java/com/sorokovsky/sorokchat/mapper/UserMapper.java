package com.sorokovsky.sorokchat.mapper;

import com.sorokovsky.sorokchat.contract.GetUserPayload;
import com.sorokovsky.sorokchat.entity.UserEntity;
import com.sorokovsky.sorokchat.model.Authority;
import com.sorokovsky.sorokchat.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapper {
    public UserModel toModel(UserEntity entity) {
        final var model = new UserModel();
        model.setId(entity.getId());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setEmail(entity.getEmail());
        model.setPassword(entity.getPassword());
        model.setFirstName(entity.getFirstName());
        model.setLastName(entity.getLastName());
        model.setMiddleName(entity.getMiddleName());
        model.setAuthorities(entity.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.name()))
                .collect(Collectors.toSet()));
        model.setContacts(entity.getContacts().stream().map(this::toModel).collect(Collectors.toSet()));
        return model;
    }

    public UserEntity toEntity(UserModel model) {
        final var entity = new UserEntity();
        entity.setId(model.getId());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setEmail(model.getEmail());
        entity.setPassword(model.getPassword());
        entity.setFirstName(model.getFirstName());
        entity.setLastName(model.getLastName());
        entity.setMiddleName(model.getMiddleName());
        entity.setAuthorities(model.getAuthorities().stream()
                .map(authority -> Authority.valueOf(authority.getAuthority()))
                .collect(Collectors.toSet()));
        entity.setContacts(model.getContacts().stream().map(this::toEntity).collect(Collectors.toSet()));
        return entity;
    }

    public GetUserPayload toGet(UserModel model, boolean withContacts) {
        return new GetUserPayload(
                model.getId(),
                model.getCreatedAt(),
                model.getUpdatedAt(),
                model.getEmail(),
                model.getFirstName(),
                model.getLastName(),
                model.getMiddleName(),
                model.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority).filter(Objects::nonNull)
                        .filter(authority -> authority.startsWith("ROLE_"))
                        .map(authority -> authority.substring("ROLE_".length()))
                        .toList(),
                withContacts ?
                        model.getContacts()
                                .stream()
                                .map(contact -> this.toGet(contact, false))
                                .collect(Collectors.toList()) :
                        null
        );
    }
}