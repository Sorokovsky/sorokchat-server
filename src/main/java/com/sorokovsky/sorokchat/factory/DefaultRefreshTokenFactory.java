package com.sorokovsky.sorokchat.factory;

import com.sorokovsky.sorokchat.model.Authority;
import com.sorokovsky.sorokchat.model.Token;
import com.sorokovsky.sorokchat.model.UserModel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Builder
public class DefaultRefreshTokenFactory implements RefreshTokenFactory {
    private final Duration lifetime;

    @Override
    public Token apply(UserModel userDetails) {
        final var now = Instant.now();
        return new Token(
                UUID.randomUUID(),
                userDetails.getUsername(),
                List.of(Authority.JWT_REFRESH.name()),
                now,
                now.plus(lifetime)
        );
    }
}
