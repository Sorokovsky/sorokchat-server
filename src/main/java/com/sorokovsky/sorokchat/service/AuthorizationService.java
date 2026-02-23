package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.contract.AuthorizedPayload;
import com.sorokovsky.sorokchat.contract.LoginPayload;
import com.sorokovsky.sorokchat.contract.NewUserPayload;
import com.sorokovsky.sorokchat.exception.authorization.BadCredentialsException;
import com.sorokovsky.sorokchat.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;

    public AuthorizedPayload register(NewUserPayload payload) {
        final var createdUser = usersService.create(payload);
        return authorize(createdUser);
    }

    public AuthorizedPayload login(LoginPayload payload) {
        final var candidateResult = usersService.getByNickname(payload.nickName())
                .orElseThrow(BadCredentialsException::new);
        if (!passwordEncoder.matches(payload.password(), candidateResult.getPassword()))
            throw new BadCredentialsException();
        return authorize(candidateResult);
    }

    private AuthorizedPayload authorize(UserModel user) {
        return new AuthorizedPayload("");
    }
}
