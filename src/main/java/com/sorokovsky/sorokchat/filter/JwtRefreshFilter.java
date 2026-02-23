package com.sorokovsky.sorokchat.filter;

import com.sorokovsky.sorokchat.deserializer.TokenDeserializer;
import com.sorokovsky.sorokchat.model.Authority;
import com.sorokovsky.sorokchat.model.Cookies;
import com.sorokovsky.sorokchat.serializer.TokenSerializer;
import com.sorokovsky.sorokchat.service.CookieService;
import com.sorokovsky.sorokchat.service.TokenService;
import com.sorokovsky.sorokchat.service.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Builder
public class JwtRefreshFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final TokenDeserializer refreshTokenDeserializer;
    private final TokenSerializer accessTokenSerializer;
    private final TokenSerializer refreshTokenSerializer;
    private final CookieService cookieService;
    private final RequestMatcher requestMatcher;
    private final UsersService usersService;
    private final MessageSource messageSource;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            final var refreshToken = cookieService.getCookie(Cookies.REFRESH_TOKEN, request)
                    .map(Cookie::getValue)
                    .flatMap(refreshTokenDeserializer)
                    .orElse(null);
            if (refreshToken == null || !refreshToken.authorities().contains(Authority.JWT_REFRESH.name())) {
                sendError(request, response);
                return;
            }
            final var userResult = usersService.getByNickname(refreshToken.subject());
            if (userResult.isEmpty()) {
                sendError(request, response);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final var locale = request.getLocale();
        final var message = messageSource.getMessage("errors.accept", new Object[0], locale);
        final var details = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, message);
        details.setTitle(message);
        final var mapper = new ObjectMapper();
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Form");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(details));

    }
}
