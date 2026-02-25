package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.model.TokenModel;
import com.sorokovsky.sorokchat.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TokenUserService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final UsersService usersService;

    @Override
    public UserModel loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken)
            throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof TokenModel token) {
            final var user = usersService.loadUserByUsername(token.subject());
            user.setAuthorities(
                    Stream.concat(token.authorities().stream().map(SimpleGrantedAuthority::new), user.getAuthorities().stream())
                            .collect(Collectors.toSet())
            );
            return user;
        }
        throw new UsernameNotFoundException("Invalid token");
    }
}
