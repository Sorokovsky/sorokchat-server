package com.sorokovsky.sorokchat.deserializer;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.sorokovsky.sorokchat.model.TokenModel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Optional;

@RequiredArgsConstructor
@Builder
public class JweTokenDeserializer extends JwtTokenDeserializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JweTokenDeserializer.class);

    private final JWEDecrypter decrypter;

    @Override
    public Optional<TokenModel> apply(String string) {
        try {
            final var encrypted = EncryptedJWT.parse(string);
            encrypted.decrypt(decrypter);
            return Optional.ofNullable(extractTokenFromClaims(encrypted.getJWTClaimsSet()));
        } catch (ParseException | JOSEException exception) {
            LOGGER.error(exception.getMessage(), exception);
            return Optional.empty();
        }
    }
}
