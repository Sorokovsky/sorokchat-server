package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.factory.AccessTokenFactory;
import com.sorokovsky.sorokchat.factory.RefreshTokenFactory;
import com.sorokovsky.sorokchat.model.TokenModel;
import com.sorokovsky.sorokchat.model.TokensModel;
import com.sorokovsky.sorokchat.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final AccessTokenFactory accessTokenFactory;
    private final RefreshTokenFactory refreshTokenFactory;

    public TokensModel generateTokens(UserModel user) {
        final var refreshToken = generateRefreshToken(user);
        final var accessToken = generateAccessToken(refreshToken);
        return new TokensModel(accessToken, refreshToken);
    }

    private TokenModel generateRefreshToken(UserModel user) {
        return refreshTokenFactory.apply(user);
    }

    private TokenModel generateAccessToken(TokenModel refreshToken) {
        return accessTokenFactory.apply(refreshToken);
    }
}
