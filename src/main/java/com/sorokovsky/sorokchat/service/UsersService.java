package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.contract.NewUserPayload;
import com.sorokovsky.sorokchat.contract.UpdateUserPayload;
import com.sorokovsky.sorokchat.entity.UserEntity;
import com.sorokovsky.sorokchat.exception.user.UserAlreadyExistsException;
import com.sorokovsky.sorokchat.exception.user.UserNotFoundException;
import com.sorokovsky.sorokchat.mapper.UserMapper;
import com.sorokovsky.sorokchat.model.Authority;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<UserModel> getById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Transactional(readOnly = true)
    public Optional<UserModel> getByNickname(String nickname) {
        return repository.findByNickname(nickname).map(mapper::toModel);
    }

    @Transactional(readOnly = true)
    public List<UserModel> getByDisplayName(String displayName) {
        return repository.findAllByDisplayName(displayName).stream().map(mapper::toModel).toList();
    }

    @Transactional
    public UserModel create(NewUserPayload payload) {
        final var candidate = getByNickname(payload.nickName());
        if (candidate.isPresent()) throw new UserAlreadyExistsException();
        final var user = UserEntity
                .builder()
                .nickname(payload.nickName())
                .password(passwordEncoder.encode(payload.password()))
                .displayName(payload.displayName())
                .authorities(new HashSet<>(List.of(Authority.ROLE_USER)))
                .build();
        try {
            return mapper.toModel(repository.save(user));
        } catch (DataIntegrityViolationException _) {
            throw new UserAlreadyExistsException();
        }
    }

    @Transactional
    public UserModel update(Long id, UpdateUserPayload payload) {
        final var user = repository.findById(id).orElseThrow(UserNotFoundException::new);
        if (payload.displayName() != null) user.setDisplayName(payload.displayName());
        if (payload.password() != null) user.setPassword(passwordEncoder.encode(payload.password()));
        if (payload.phoneNumber() != null) user.setPhoneNumber(payload.phoneNumber());
        if (payload.nickname() != null) {
            if (repository.existsByNickname(payload.nickname()) && !user.getNickname().equals(payload.nickname()))
                throw new UserAlreadyExistsException();
            user.setNickname(payload.nickname());
        }
        if (payload.email() != null) {
            if (repository.existsByEmail(payload.email()) && !user.getEmail().equals(payload.email()))
                throw new UserAlreadyExistsException();
            user.setEmail(payload.email());
        }
        try {
            return mapper.toModel(repository.save(user));
        } catch (DataIntegrityViolationException _) {
            throw new UserAlreadyExistsException();
        }
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
