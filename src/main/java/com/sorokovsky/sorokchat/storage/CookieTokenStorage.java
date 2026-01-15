package com.sorokovsky.sorokchat.storage;

import com.sorokovsky.sorokchat.deserialization.TokenDeserializer;
import com.sorokovsky.sorokchat.model.Token;
import com.sorokovsky.sorokchat.serialization.TokenSerializer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Builder
public class CookieTokenStorage implements TokenStorage {
    private final TokenSerializer serializer;
    private final TokenDeserializer deserializer;
    private final String cookieName;

    private static Cookie generateCookie(String name, String value, int maxAge) {
        final var cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setDomain(null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }

    @Override
    public Optional<Token> getToken(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();
        return Stream.of(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .flatMap(deserializer);
    }

    @Override
    public void setToken(Token token, HttpServletResponse response) {
        final var stringToken = serializer.apply(token).orElse(null);
        if (stringToken == null) return;
        final var maxAge = (int) ChronoUnit.SECONDS.between(token.createdAt(), token.expiresAt());
        response.addCookie(generateCookie(cookieName, stringToken, maxAge));
    }

    @Override
    public void clearToken(HttpServletResponse response) {
        response.addCookie(generateCookie(cookieName, "", 0));
    }
}
