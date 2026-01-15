package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@RequiredArgsConstructor
public class JwtUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private static final String MESSAGE_CODE = "error.user.not-found";

    private final UsersService usersService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken) throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof Token token) {
            final var user = usersService.getByEmail(token.email())
                    .orElseThrow(() -> new UsernameNotFoundException(MESSAGE_CODE));
            user.getAuthorities().addAll(token.authorities().stream().map(SimpleGrantedAuthority::new).toList());
            return user;
        }
        throw new UsernameNotFoundException(MESSAGE_CODE);
    }
}
