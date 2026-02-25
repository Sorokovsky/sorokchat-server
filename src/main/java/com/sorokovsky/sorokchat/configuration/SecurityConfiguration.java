package com.sorokovsky.sorokchat.configuration;

import com.sorokovsky.sorokchat.configurer.JwtConfigurer;
import com.sorokovsky.sorokchat.deserializer.JweTokenDeserializer;
import com.sorokovsky.sorokchat.deserializer.JwsTokenDeserializer;
import com.sorokovsky.sorokchat.entrypoint.ForbiddenEntryPoint;
import com.sorokovsky.sorokchat.entrypoint.UnauthorizedEntryPoint;
import com.sorokovsky.sorokchat.interceptors.JwtChannelInterceptor;
import com.sorokovsky.sorokchat.model.Authority;
import com.sorokovsky.sorokchat.serializer.JweTokenSerializer;
import com.sorokovsky.sorokchat.serializer.JwsTokenSerializer;
import com.sorokovsky.sorokchat.service.CookieService;
import com.sorokovsky.sorokchat.service.TokenService;
import com.sorokovsky.sorokchat.service.TokenUserService;
import com.sorokovsky.sorokchat.service.UsersService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CorsConfigurationSource corsConfigurationSource,
            JwtConfigurer jwtConfigurer,
            UnauthorizedEntryPoint unauthorizedEntryPoint,
            PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider,
            ForbiddenEntryPoint forbiddenEntryPoint
    ) {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/ws/**", "/swagger-ui/**", "/v3/api-docs/**", "/openapi.yaml").permitAll()
                        .requestMatchers("/authorization/register", "/authorization/login").anonymous()
                        .requestMatchers(HttpMethod.DELETE, "/authorization/logout").hasAnyAuthority(Authority.JWT_LOGOUT.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(unauthorizedEntryPoint)
                        .accessDeniedHandler(forbiddenEntryPoint)
                )
                .authenticationProvider(preAuthenticatedAuthenticationProvider)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource));
        http.apply(jwtConfigurer);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://127.0.0.1:4200", "https://sorokchat-client.vercel.app"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Content-Disposition"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtConfigurer jwtConfigurer(
            JwsTokenDeserializer accessTokenDeserializer,
            UnauthorizedEntryPoint unauthorizedEntryPoint,
            JwsTokenSerializer accessTokenSerializer,
            JweTokenSerializer refreshTokenSerializer,
            JweTokenDeserializer refreshTokenDeserializer,
            CookieService cookieService,
            UsersService usersService,
            TokenService tokenService
    ) {
        return JwtConfigurer
                .builder()
                .accessTokenDeserializer(accessTokenDeserializer)
                .authenticationEntryPoint(unauthorizedEntryPoint)
                .accessTokenSerializer(accessTokenSerializer)
                .cookieService(cookieService)
                .refreshMatcher(PathPatternRequestMatcher.pathPattern(HttpMethod.PUT, "/authorization/refresh-tokens"))
                .refreshTokenSerializer(refreshTokenSerializer)
                .refreshTokenDeserializer(refreshTokenDeserializer)
                .usersService(usersService)
                .tokenService(tokenService)
                .build();
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider(TokenUserService tokenUserService) {
        final var provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(tokenUserService);
        return provider;
    }

    @Bean
    public JwtChannelInterceptor jwtChannelInterceptor(
            JwsTokenDeserializer jwsTokenDeserializer,
            UsersService usersService
    ) {
        return new JwtChannelInterceptor(jwsTokenDeserializer, usersService);
    }
}
