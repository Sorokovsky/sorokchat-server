package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.contract.LoginPayload;
import com.sorokovsky.sorokchat.contract.NewUserPayload;
import com.sorokovsky.sorokchat.exception.user.BadCredentialsException;
import com.sorokovsky.sorokchat.factory.AccessTokenFactory;
import com.sorokovsky.sorokchat.factory.RefreshTokenFactory;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.storage.TokenStorage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class AuthorizationService {
    private final AccessTokenFactory accessTokenFactory;
    private final RefreshTokenFactory refreshTokenFactory;
    private final TokenStorage accessTokenStorage;
    private final TokenStorage refreshTokenStorage;
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;

    public UserModel register(NewUserPayload newUser, HttpServletResponse response) {
        final var user = usersService.create(newUser);
        authorize(user, response);
        return user;
    }

    public UserModel login(LoginPayload loginPayload, HttpServletResponse response) {
        final var candidate = usersService.getByEmail(loginPayload.email())
                .orElseThrow(BadCredentialsException::new);
        if (!passwordEncoder.matches(loginPayload.password(), candidate.getPassword()))
            throw new BadCredentialsException();
        authorize(candidate, response);
        return candidate;
    }

    public void logout(HttpServletResponse response) {
        refreshTokenStorage.clearToken(response);
        accessTokenStorage.clearToken(response);
    }

    private void authorize(UserModel user, HttpServletResponse response) {
        final var refreshToken = refreshTokenFactory.apply(user);
        final var accessToken = accessTokenFactory.apply(refreshToken);
        accessTokenStorage.setToken(accessToken, response);
        refreshTokenStorage.setToken(refreshToken, response);

    }
}
