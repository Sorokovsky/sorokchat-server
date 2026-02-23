package com.sorokovsky.sorokchat.configurer;

import com.sorokovsky.sorokchat.controller.JwtAuthenticationConverter;
import com.sorokovsky.sorokchat.deserializer.TokenDeserializer;
import com.sorokovsky.sorokchat.filter.JwtRefreshFilter;
import com.sorokovsky.sorokchat.serializer.TokenSerializer;
import com.sorokovsky.sorokchat.service.CookieService;
import com.sorokovsky.sorokchat.service.TokenService;
import com.sorokovsky.sorokchat.service.UsersService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Builder
@RequiredArgsConstructor
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final CookieService cookieService;
    private final TokenDeserializer accessTokenDeserializer;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final TokenSerializer accessTokenSerializer;
    private final TokenSerializer refreshTokenSerializer;
    private final TokenDeserializer refreshTokenDeserializer;
    private final RequestMatcher refreshMatcher;
    private final UsersService usersService;
    private final TokenService tokenService;

    @Override
    public void init(HttpSecurity builder) {
        super.init(builder);
    }

    @Override
    public void configure(HttpSecurity builder) {
        final var manager = builder.getSharedObject(AuthenticationManager.class);
        final var converter = JwtAuthenticationConverter
                .builder()
                .deserializer(accessTokenDeserializer)
                .build();
        final var authenticationFilter = new AuthenticationFilter(manager, converter);
        authenticationFilter.setSuccessHandler((_, _, _) -> {
        });
        authenticationFilter.setFailureHandler(authenticationEntryPoint::commence);
        final var messageSource = builder.getSharedObject(ApplicationContext.class).getBean(MessageSource.class);
        final var refreshFilter = JwtRefreshFilter
                .builder()
                .refreshTokenSerializer(refreshTokenSerializer)
                .accessTokenSerializer(accessTokenSerializer)
                .messageSource(messageSource)
                .refreshTokenDeserializer(refreshTokenDeserializer)
                .requestMatcher(refreshMatcher)
                .cookieService(cookieService)
                .usersService(usersService)
                .tokenService(tokenService)
                .build();
        builder.addFilterBefore(refreshFilter, UsernamePasswordAuthenticationFilter.class);
        builder.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
