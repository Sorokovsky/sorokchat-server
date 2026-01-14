package com.sorokovsky.sorokchat.serialization;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.SignedJWT;
import com.sorokovsky.sorokchat.model.Token;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RequiredArgsConstructor
@Builder
public class JwsTokenSerializer extends JwtTokenSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwsTokenSerializer.class);

    private final JWSAlgorithm algorithm;
    private final JWSSigner signer;

    @Override
    public Optional<String> apply(Token token) {
        final var header = new JWSHeader.Builder(algorithm)
                .keyID(token.id().toString())
                .build();
        final var claims = getClaimsFromToken(token);
        final var signed = new SignedJWT(header, claims);
        try {
            signed.sign(signer);
            return Optional.ofNullable(signed.serialize());
        } catch (JOSEException exception) {
            LOGGER.error(exception.getMessage(), exception);
            return Optional.empty();
        }
    }
}
