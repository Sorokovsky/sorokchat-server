package com.sorokovsky.sorokchat.deserializer;

import com.sorokovsky.sorokchat.model.TokenModel;

import java.util.Optional;
import java.util.function.Function;

public interface TokenDeserializer extends Function<String, Optional<TokenModel>> {
}
