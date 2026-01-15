package com.sorokovsky.sorokchat.filter;

import com.sorokovsky.sorokchat.factory.AccessTokenFactory;
import com.sorokovsky.sorokchat.factory.RefreshTokenFactory;
import com.sorokovsky.sorokchat.service.UsersService;
import com.sorokovsky.sorokchat.storage.TokenStorage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Builder
public class JwtRefreshFilter extends OncePerRequestFilter {
    private final TokenStorage refreshTokenStorage;
    private final TokenStorage accessTokenStorage;
    private final AccessTokenFactory accessTokenFactory;
    private final RefreshTokenFactory refreshTokenFactory;
    private final UsersService usersService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final var refreshToken = refreshTokenStorage.getToken(request).orElse(null);
        final var accessToken = accessTokenStorage.getToken(request).orElse(null);
        if (accessToken == null) {
            if (refreshToken != null) {
                final var user = usersService.getByEmail(refreshToken.email()).orElse(null);
                if (user != null) {
                    final var newRefreshToken = refreshTokenFactory.apply(user);
                    final var newAccessToken = accessTokenFactory.apply(newRefreshToken);
                    refreshTokenStorage.setToken(newRefreshToken, response);
                    accessTokenStorage.setToken(newAccessToken, response);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
