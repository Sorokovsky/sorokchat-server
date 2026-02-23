package com.sorokovsky.sorokchat.factory;

import com.sorokovsky.sorokchat.model.Authority;
import com.sorokovsky.sorokchat.model.TokenModel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Builder
public class DefaultAccessTokenFactory implements AccessTokenFactory {
    private final Duration lifetime;

    @Override
    public TokenModel apply(TokenModel token) {
        final var authorities = List.of(Authority.JWT_ACCESS.name(), Authority.JWT_LOGOUT.name());
        final var now = Instant.now();
        return new TokenModel(token.id(), token.subject(), authorities, now, now.plus(lifetime));
    }
}
