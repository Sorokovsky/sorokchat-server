package com.sorokovsky.sorokchat.interceptors;

import com.sorokovsky.sorokchat.deserialization.TokenDeserializer;
import com.sorokovsky.sorokchat.exception.user.BadCredentialsException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
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
    private final TokenDeserializer deserializer;

    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
        final var accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            final var header = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
            final var bearerPrefix = "Bearer ";
            if (header == null || !header.startsWith(bearerPrefix)) {
                return null;
            }
            final var token = header.substring(bearerPrefix.length());
            try {
                final var model = deserializer.apply(token)
                        .orElseThrow(BadCredentialsException::new);
                final var authorization = new PreAuthenticatedAuthenticationToken(model, model);
                accessor.setUser(authorization);
                SecurityContextHolder.getContext().setAuthentication(authorization);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        return message;
    }
}
