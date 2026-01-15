package com.sorokovsky.sorokchat.configurer;

import com.sorokovsky.sorokchat.converter.JwtAuthenticationConverter;
import com.sorokovsky.sorokchat.factory.AccessTokenFactory;
import com.sorokovsky.sorokchat.factory.RefreshTokenFactory;
import com.sorokovsky.sorokchat.filter.JwtRefreshFilter;
import com.sorokovsky.sorokchat.service.JwtUserDetailsService;
import com.sorokovsky.sorokchat.service.UsersService;
import com.sorokovsky.sorokchat.storage.TokenStorage;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;

@RequiredArgsConstructor
@Builder
public class JwtAuthenticationConfigurer implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenStorage accessTokenStorage;
    private final TokenStorage refreshTokenStorage;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final AccessTokenFactory accessTokenFactory;
    private final RefreshTokenFactory refreshTokenFactory;
    private final UsersService usersService;

    @Override
    public void init(HttpSecurity builder) {
    }

    @Override
    public void configure(HttpSecurity builder) {
        final var converter = new JwtAuthenticationConverter(accessTokenStorage, refreshTokenStorage);
        final var manager = builder.getSharedObject(AuthenticationManager.class);
        final var authenticationFilter = new AuthenticationFilter(manager, converter);
        final var refreshFilter = new JwtRefreshFilter(
                refreshTokenStorage,
                accessTokenStorage,
                accessTokenFactory,
                refreshTokenFactory,
                usersService
        );
        authenticationFilter.setSuccessHandler((_, _, _) -> {
        });
        authenticationFilter.setFailureHandler(authenticationEntryPoint::commence);
        final var provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(jwtUserDetailsService);
        builder.addFilterAfter(authenticationFilter, CsrfFilter.class);
        builder.addFilterBefore(refreshFilter, CsrfFilter.class);
        builder.authenticationProvider(provider);
    }
}
