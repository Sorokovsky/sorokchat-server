package com.sorokovsky.sorokchat.factory;

import com.sorokovsky.sorokchat.model.Token;

import java.util.function.Function;

public interface AccessTokenFactory extends Function<Token, Token> {
}
