package com.sorokovsky.sorokchat.storage;

import com.sorokovsky.sorokchat.model.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface TokenStorage {
    Optional<Token> getToken(HttpServletRequest request);

    void setToken(Token token, HttpServletResponse response);

    void clearToken(HttpServletResponse response);
}
