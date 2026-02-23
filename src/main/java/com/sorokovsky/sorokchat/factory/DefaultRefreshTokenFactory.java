package com.sorokovsky.sorokchat.factory;

import com.sorokovsky.sorokchat.model.Authority;
import com.sorokovsky.sorokchat.model.TokenModel;
import com.sorokovsky.sorokchat.model.UserModel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@RequiredArgsConstructor
public class DefaultRefreshTokenFactory implements RefreshTokenFactory {
    private final Duration lifetime;

    @Override
    public TokenModel apply(UserModel user) {
        final var now = Instant.now();
        final var authorities = List.of(Authority.JWT_REFRESH.name());
        return new TokenModel(UUID.randomUUID(), user.getNickname(), authorities, now, now.plus(lifetime));
    }
}
