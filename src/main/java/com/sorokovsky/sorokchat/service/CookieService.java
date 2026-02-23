package com.sorokovsky.sorokchat.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CookieService {
    public void setCookie(String key, String value, int maxAge, HttpServletResponse response) {
        response.addCookie(generateCookie(key, value, maxAge));
    }

    public Optional<Cookie> getCookie(String key, HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();
        return Stream.of(request.getCookies())
                .filter(cookie -> cookie.getName().equals(key))
                .findFirst();
    }

    public void clearCookie(String key, HttpServletResponse response) {
        response.addCookie(generateCookie(key, "", 0));
    }

    private Cookie generateCookie(String key, String value, int maxAge) {
        final var cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setDomain(null);
        return cookie;
    }
}
