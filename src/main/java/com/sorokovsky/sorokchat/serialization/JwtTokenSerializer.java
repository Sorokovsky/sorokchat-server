package com.sorokovsky.sorokchat.serialization;

import com.nimbusds.jwt.JWTClaimsSet;
import com.sorokovsky.sorokchat.model.Token;

import java.util.Date;

public abstract class JwtTokenSerializer implements TokenSerializer {
    protected JWTClaimsSet getClaimsFromToken(Token token) {
        return new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .issueTime(Date.from(token.createdAt()))
                .subject(token.email())
                .expirationTime(Date.from(token.expiresAt()))
                .claim("authorities", token.authorities())
                .build();
    }
}
