package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.contract.AuthorizedPayload;
import com.sorokovsky.sorokchat.contract.LoginPayload;
import com.sorokovsky.sorokchat.contract.NewUserPayload;
import com.sorokovsky.sorokchat.exception.authorization.BadCredentialsException;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.serializer.JweTokenSerializer;
import com.sorokovsky.sorokchat.serializer.JwsTokenSerializer;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private static final String REFRESH_COOKIE_NAME = "__Host-refresh-token";

    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final CookieService cookieService;
    private final TokenService tokenService;
    private final JwsTokenSerializer jwsTokenSerializer;
    private final JweTokenSerializer jweTokenSerializer;

    public AuthorizedPayload register(NewUserPayload payload, HttpServletResponse response) {
        final var createdUser = usersService.create(payload);
        return authorize(createdUser, response);
    }

    public AuthorizedPayload login(LoginPayload payload, HttpServletResponse response) {
        final var candidateResult = usersService.getByNickname(payload.nickname())
                .orElseThrow(BadCredentialsException::new);
        if (!passwordEncoder.matches(payload.password(), candidateResult.getPassword()))
            throw new BadCredentialsException();
        return authorize(candidateResult, response);
    }

    private AuthorizedPayload authorize(UserModel user, HttpServletResponse response) {
        final var tokens = tokenService.generateTokens(user);
        final var accessToken = tokens.accessToken();
        final var refreshToken = tokens.refreshToken();
        final var maxAge = (int) ChronoUnit.SECONDS.between(refreshToken.createdAt(), refreshToken.expiresAt());
        cookieService.setCookie(REFRESH_COOKIE_NAME, jweTokenSerializer.apply(refreshToken), maxAge, response);
        return new AuthorizedPayload(jwsTokenSerializer.apply(accessToken));
    }

    public void logout(HttpServletResponse response) {
        cookieService.clearCookie(REFRESH_COOKIE_NAME, response);
    }
}
