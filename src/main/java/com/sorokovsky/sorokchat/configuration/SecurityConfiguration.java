package com.sorokovsky.sorokchat.configuration;

import com.sorokovsky.sorokchat.configurer.JwtAuthenticationConfigurer;
import com.sorokovsky.sorokchat.factory.DefaultAccessTokenFactory;
import com.sorokovsky.sorokchat.factory.DefaultRefreshTokenFactory;
import com.sorokovsky.sorokchat.model.Authority;
import com.sorokovsky.sorokchat.point.UnauthenticatedEntryPoint;
import com.sorokovsky.sorokchat.service.AuthorizationService;
import com.sorokovsky.sorokchat.service.JwtUserDetailsService;
import com.sorokovsky.sorokchat.service.UsersService;
import com.sorokovsky.sorokchat.storage.BearerTokenStorage;
import com.sorokovsky.sorokchat.storage.CookieTokenStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConfigurer jwtAuthenticationConfigurer,
            UnauthenticatedEntryPoint unauthenticatedEntryPoint
    ) {
        http
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/swagger-ui/**", "/openapi.yaml", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/authorization/login", "/authorization/register").anonymous()
                        .requestMatchers("/authorization/logout").hasAuthority(Authority.JWT_LOGOUT.name())
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer -> configurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(unauthenticatedEntryPoint)
                );
        http.apply(jwtAuthenticationConfigurer);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthorizationService authorizationService(
            UsersService usersService,
            DefaultAccessTokenFactory accessTokenFactory,
            DefaultRefreshTokenFactory refreshTokenFactory,
            CookieTokenStorage refreshTokenStorage,
            BearerTokenStorage accessTokenStorage,
            PasswordEncoder passwordEncoder
    ) {
        return AuthorizationService
                .builder()
                .usersService(usersService)
                .accessTokenFactory(accessTokenFactory)
                .refreshTokenFactory(refreshTokenFactory)
                .refreshTokenStorage(refreshTokenStorage)
                .accessTokenStorage(accessTokenStorage)
                .passwordEncoder(passwordEncoder)
                .build();
    }

    @Bean
    public JwtUserDetailsService jwtUserDetailsService(UsersService usersService) {
        return new JwtUserDetailsService(usersService);
    }

    @Bean
    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(
            JwtUserDetailsService jwtUserDetailsService,
            UnauthenticatedEntryPoint unauthenticatedEntryPoint,
            BearerTokenStorage accessTokenStorage,
            CookieTokenStorage refreshTokenStorage,
            UsersService usersService,
            DefaultAccessTokenFactory accessTokenFactory,
            DefaultRefreshTokenFactory refreshTokenFactory
    ) {
        return JwtAuthenticationConfigurer
                .builder()
                .authenticationEntryPoint(unauthenticatedEntryPoint)
                .jwtUserDetailsService(jwtUserDetailsService)
                .accessTokenStorage(accessTokenStorage)
                .refreshTokenStorage(refreshTokenStorage)
                .usersService(usersService)
                .accessTokenFactory(accessTokenFactory)
                .refreshTokenFactory(refreshTokenFactory)
                .build();
    }
}
