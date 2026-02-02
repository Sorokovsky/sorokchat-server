package com.sorokovsky.sorokchat.interceptors;

import com.sorokovsky.sorokchat.deserialization.TokenDeserializer;
import com.sorokovsky.sorokchat.exception.user.BadCredentialsException;
import com.sorokovsky.sorokchat.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtChannelInterceptor.class);

    private final TokenDeserializer deserializer;
    private final UsersService usersService;

    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
        final var accessor = StompHeaderAccessor.wrap(message);
        LOGGER.info("Pre-Send: {}", accessor.getCommand());
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            final var header = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
            final var bearerPrefix = "Bearer ";
            if (header == null || !header.startsWith(bearerPrefix)) {
                LOGGER.debug("Authorization header is missing");
                throw new BadCredentialsException();
            }
            final var token = header.substring(bearerPrefix.length());
            LOGGER.debug("ACCESS TOKEN: {}", token);
            try {
                final var model = deserializer.apply(token)
                        .orElseThrow(BadCredentialsException::new);
                final var user = usersService.getByEmail(model.email())
                        .orElseThrow(BadCredentialsException::new);
                LOGGER.debug("PARSED TOKEN: {}", model);
                final var authorization = new PreAuthenticatedAuthenticationToken(user, user, user.getAuthorities());
                accessor.setUser(authorization);
                SecurityContextHolder.getContext().setAuthentication(authorization);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        return message;
    }
}
