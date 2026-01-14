package com.sorokovsky.sorokchat.serialization;

import com.sorokovsky.sorokchat.model.Token;

import java.util.Optional;
import java.util.function.Function;

public interface TokenSerializer extends Function<Token, Optional<String>> {
}
