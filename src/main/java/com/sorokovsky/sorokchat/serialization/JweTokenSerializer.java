package com.sorokovsky.sorokchat.serialization;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.sorokovsky.sorokchat.model.Token;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RequiredArgsConstructor
@Builder
public class JweTokenSerializer extends JwtTokenSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JweTokenSerializer.class);

    private final JWEAlgorithm algorithm;
    private final EncryptionMethod method;
    private final JWEEncrypter encrypter;

    @Override
    public Optional<String> apply(Token token) {
        final var header = new JWEHeader.Builder(algorithm, method)
                .keyID(token.id().toString())
                .build();
        final var claims = getClaimsFromToken(token);
        final var encrypted = new EncryptedJWT(header, claims);
        try {
            encrypted.encrypt(encrypter);
            return Optional.ofNullable(encrypted.serialize());
        } catch (JOSEException exception) {
            LOGGER.error(exception.getMessage(), exception);
            return Optional.empty();
        }
    }
}
