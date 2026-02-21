package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.contract.NewUserPayload;
import com.sorokovsky.sorokchat.exception.user.UserAlreadyExists;
import com.sorokovsky.sorokchat.exception.user.UserNotFoundException;
import com.sorokovsky.sorokchat.mapper.UserMapper;
import com.sorokovsky.sorokchat.model.Authority;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
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
    public Optional<UserModel> getByEmail(String email) {
        return repository
                .findByEmail(email)
                .map(mapper::toModel);
    }

    @Transactional(readOnly = true)
    public Optional<UserModel> getById(Long id) {
        return repository
                .findById(id)
                .map(mapper::toModel);
    }

    @Transactional
    public UserModel create(NewUserPayload newUser) {
        final var candidate = getByEmail(newUser.email()).orElse(null);
        if (candidate != null) throw new UserAlreadyExists();
        final var now = Date.from(Instant.now());
        final var user = new UserModel();
        user.setId(null);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setEmail(newUser.email());
        user.setPassword(passwordEncoder.encode(newUser.password()));
        user.setFirstName(newUser.firstName());
        user.setLastName(newUser.lastName());
        user.setMiddleName(newUser.middleName());
        user.setAuthorities(new HashSet<>(List.of(new SimpleGrantedAuthority(Authority.ROLE_USER.name()))));
        return mapper.toModel(repository.save(mapper.toEntity(user)));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("error.user.already.exists"));
    }

    @Transactional
    public UserModel addToContact(UserModel user, String contactEmail) {
        final var candidate = repository.findByEmail(contactEmail).orElseThrow(UserNotFoundException::new);
        final var userEntity = repository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        if (!userEntity.getContacts().contains(candidate)) {
            userEntity.getContacts().add(candidate);
            userEntity.setUpdatedAt(Date.from(Instant.now()));
            return mapper.toModel(repository.save(userEntity));
        }
        return user;
    }

    @Transactional
    public UserModel removeFromContact(UserModel user, Long contactId) {
        final var userEntity = repository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        final var candidate = repository.findById(contactId);
        if (candidate.isPresent() && userEntity.getContacts().contains(candidate.get())) {
            userEntity.getContacts().remove(candidate.get());
            userEntity.setUpdatedAt(Date.from(Instant.now()));
            return mapper.toModel(repository.save(userEntity));
        }
        return user;
    }
}
