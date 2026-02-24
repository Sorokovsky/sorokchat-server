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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {
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

    @Transactional(readOnly = true)
    public Optional<UserModel> getByNicknamePartial(String nickname) {
        return repository.findByNicknameLikeIgnoreCase(nickname).map(mapper::toModel);
    }

    @Transactional(readOnly = true)
    public List<UserModel> getByDisplayNamePartial(String displayName) {
        return repository.findAllByDisplayNameLikeIgnoreCase(displayName).stream().map(mapper::toModel).toList();
    }

    @Transactional
    public UserModel create(NewUserPayload payload) {
        final var candidate = getByNickname(payload.nickname());
        if (candidate.isPresent()) throw new UserAlreadyExistsException();
        final var user = UserEntity
                .builder()
                .nickname(payload.nickname())
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
        if (payload.displayName().isPresent()) user.setDisplayName(payload.displayName().get());
        if (payload.password().isPresent()) user.setPassword(passwordEncoder.encode(payload.password().get()));
        if (payload.phoneNumber().isPresent()) user.setPhoneNumber(payload.phoneNumber().get());
        if (payload.nickname().isPresent()) {
            if (repository.existsByNickname(payload.nickname().get()) && !user.getNickname().equals(payload.nickname().get()))
                throw new UserAlreadyExistsException();
            user.setNickname(payload.nickname().get());
        }
        if (payload.email().isPresent()) {
            if (repository.existsByEmail(payload.email().get()) && !user.getEmail().equals(payload.email().get()))
                throw new UserAlreadyExistsException();
            user.setEmail(payload.email().get());
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

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getByNickname(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
