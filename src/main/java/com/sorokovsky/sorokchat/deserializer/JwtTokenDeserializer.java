package com.sorokovsky.sorokchat.deserializer;

import com.nimbusds.jwt.JWTClaimsSet;
import com.sorokovsky.sorokchat.model.TokenModel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.text.ParseException;
import java.util.UUID;

@RequiredArgsConstructor
@Builder
public abstract class JwtTokenDeserializer implements TokenDeserializer {
    protected TokenModel extractTokenFromClaims(JWTClaimsSet claims) throws ParseException {
        return new TokenModel(
                UUID.fromString(claims.getJWTID()),
                claims.getSubject(),
                claims.getStringListClaim("authorities"),
                claims.getIssueTime().toInstant(),
                claims.getExpirationTime().toInstant()
        );
    }
}
