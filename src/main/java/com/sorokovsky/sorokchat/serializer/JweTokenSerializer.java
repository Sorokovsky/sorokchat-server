package com.sorokovsky.sorokchat.serializer;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.sorokovsky.sorokchat.exception.base.InternalServerException;
import com.sorokovsky.sorokchat.model.TokenModel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@Builder
public class JweTokenSerializer extends JwtTokenSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JweTokenSerializer.class);

    private final JWEAlgorithm algorithm;
    private final EncryptionMethod method;
    private final JWEEncrypter encrypter;

    @Override
    public String apply(TokenModel token) {
        final var header = new JWEHeader.Builder(algorithm, method).keyID(token.id().toString()).build();
        final var claims = getClaimsFromToken(token);
        final var encrypted = new EncryptedJWT(header, claims);
        try {
            encrypted.encrypt(encrypter);
            return encrypted.serialize();
        } catch (JOSEException exception) {
            LOGGER.error(exception.getMessage(), exception);
            throw new InternalServerException(exception);
        }
    }
}
