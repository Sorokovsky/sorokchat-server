package com.sorokovsky.sorokchat.point;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class UnauthenticatedEntryPoint implements AuthenticationEntryPoint {
    private final MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        final var locale = request.getLocale();
        final var message = authException.getLocalizedMessage();
        final var details = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, message);
        details.setTitle(messageSource.getMessage("error.unauthenticated", new Object[0], locale));
        final var mapper = new ObjectMapper();
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(details));
    }
}
