package com.sorokovsky.sorokchat.factory;

import com.sorokovsky.sorokchat.model.Token;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface RefreshTokenFactory extends Function<UserDetails, Token> {
}
