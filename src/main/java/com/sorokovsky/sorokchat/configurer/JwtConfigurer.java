package com.sorokovsky.sorokchat.configurer;

import com.sorokovsky.sorokchat.controller.JwtAuthenticationConverter;
import com.sorokovsky.sorokchat.deserializer.TokenDeserializer;
import com.sorokovsky.sorokchat.service.CookieService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Builder
@RequiredArgsConstructor
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final CookieService cookieService;
    private final TokenDeserializer deserializer;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void init(HttpSecurity builder) {
        super.init(builder);
    }

    @Override
    public void configure(HttpSecurity builder) {
        final var manager = builder.getSharedObject(AuthenticationManager.class);
        final var converter = JwtAuthenticationConverter
                .builder()
                .deserializer(deserializer)
                .build();
        final var filter = new AuthenticationFilter(manager, converter);
        filter.setSuccessHandler((_, _, _) -> {
        });
        filter.setFailureHandler(authenticationEntryPoint::commence);
        builder.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
