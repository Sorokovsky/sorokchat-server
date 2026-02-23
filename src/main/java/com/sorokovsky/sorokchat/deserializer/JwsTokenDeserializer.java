package com.sorokovsky.sorokchat.deserializer;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.sorokovsky.sorokchat.model.TokenModel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Optional;

@RequiredArgsConstructor
@Builder
public class JwsTokenDeserializer extends JwtTokenDeserializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwsTokenDeserializer.class);

    private final JWSVerifier verifier;

    @Override
    public Optional<TokenModel> apply(String string) {
        try {
            final var signed = SignedJWT.parse(string);
            signed.verify(verifier);
            return Optional.ofNullable(extractTokenFromClaims(signed.getJWTClaimsSet()));
        } catch (ParseException | JOSEException exception) {
            LOGGER.error(exception.getMessage(), exception);
            return Optional.empty();
        }
    }
}
