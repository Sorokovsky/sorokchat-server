package com.sorokovsky.sorokchat.factory;

import com.sorokovsky.sorokchat.model.Token;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Builder
public class DefaultRefreshTokenFactory implements RefreshTokenFactory {
    private final Duration lifetime;

    @Override
    public Token apply(UserDetails userDetails) {
        final var now = Instant.now();
        return new Token(
                UUID.randomUUID(),
                userDetails.getUsername(),
                Stream.concat(
                        userDetails.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority),
                        Stream.of("JWT_REFRESH")
                ).toList(),
                now,
                now.plus(lifetime)
        );
    }
}
