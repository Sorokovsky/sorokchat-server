package com.sorokovsky.sorokchat.deserialization;

import com.sorokovsky.sorokchat.model.Token;

import java.util.Optional;
import java.util.function.Function;

public interface TokenDeserializer extends Function<String, Optional<Token>> {
}
