package com.sorokovsky.sorokchat.factory;

import com.sorokovsky.sorokchat.model.TokenModel;

import java.util.function.Function;

public interface AccessTokenFactory extends Function<TokenModel, TokenModel> {
}
