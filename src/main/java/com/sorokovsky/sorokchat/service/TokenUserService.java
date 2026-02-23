package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.model.TokenModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenUserService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final UserDetailsService usersService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken)
            throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof TokenModel token) {
            return usersService.loadUserByUsername(token.subject());
        }
        throw new UsernameNotFoundException("Invalid token");
    }
}
