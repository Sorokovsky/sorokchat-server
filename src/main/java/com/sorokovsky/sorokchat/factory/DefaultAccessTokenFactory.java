package com.sorokovsky.sorokchat.factory;

import com.sorokovsky.sorokchat.model.Token;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Builder
public class DefaultAccessTokenFactory implements AccessTokenFactory {
    private final Duration lifetime;

    @Override
    public Token apply(Token token) {
        final var now = Instant.now();
        return new Token(
                token.id(),
                token.email(),
                Stream.concat(token.authorities().stream(), Stream.of("JWT_ACCESS", "JWT_LOGOUT")).toList(),
                now,
                now.plus(lifetime)
        );
    }
}
