package com.sorokovsky.sorokchat.deserialization;

import com.nimbusds.jwt.JWTClaimsSet;
import com.sorokovsky.sorokchat.model.Token;

import java.text.ParseException;
import java.util.UUID;

public abstract class JwtTokenDeserializer implements TokenDeserializer {
    protected Token extractTokenFromClaims(JWTClaimsSet claims) throws ParseException {
        final var authorities = claims.getStringListClaim("authorities");
        return new Token(
                UUID.fromString(claims.getJWTID()),
                claims.getSubject(),
                authorities,
                claims.getIssueTime().toInstant(),
                claims.getExpirationTime().toInstant()
        );
    }
}
