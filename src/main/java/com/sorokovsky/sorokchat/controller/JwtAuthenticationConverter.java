package com.sorokovsky.sorokchat.controller;

import com.sorokovsky.sorokchat.deserializer.TokenDeserializer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@RequiredArgsConstructor
@Builder
public class JwtAuthenticationConverter implements AuthenticationConverter {
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenDeserializer deserializer;

    @Override
    public @Nullable Authentication convert(HttpServletRequest request) {
        final var header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_PREFIX)) return null;
        final var token = deserializer.apply(header.substring(BEARER_PREFIX.length())).orElse(null);
        if (token == null) return null;
        return new PreAuthenticatedAuthenticationToken(token, token, token.authorities()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList());
    }
}
