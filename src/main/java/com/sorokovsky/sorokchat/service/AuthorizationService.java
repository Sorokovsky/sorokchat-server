package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.contract.AuthorizedPayload;
import com.sorokovsky.sorokchat.contract.LoginPayload;
import com.sorokovsky.sorokchat.contract.NewUserPayload;
import com.sorokovsky.sorokchat.exception.authorization.BadCredentialsException;
import com.sorokovsky.sorokchat.model.UserModel;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;

    public AuthorizedPayload register(NewUserPayload payload, HttpServletResponse response) {
        final var createdUser = usersService.create(payload);
        return authorize(createdUser);
    }

    public AuthorizedPayload login(LoginPayload payload, HttpServletResponse response) {
        final var candidateResult = usersService.getByNickname(payload.nickName())
                .orElseThrow(BadCredentialsException::new);
        if (!passwordEncoder.matches(payload.password(), candidateResult.getPassword()))
            throw new BadCredentialsException();
        return authorize(candidateResult);
    }

    private AuthorizedPayload authorize(UserModel user) {
        return new AuthorizedPayload(user.getNickname());
    }

    public void logout(HttpServletResponse response) {

    }
}
