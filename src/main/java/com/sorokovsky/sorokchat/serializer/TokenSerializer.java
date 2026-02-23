package com.sorokovsky.sorokchat.serializer;

import com.sorokovsky.sorokchat.model.TokenModel;

import java.util.function.Function;

public interface TokenSerializer extends Function<TokenModel, String> {
}
