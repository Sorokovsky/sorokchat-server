package com.sorokovsky.sorokchat.serializer;

import com.nimbusds.jwt.JWTClaimsSet;
import com.sorokovsky.sorokchat.model.TokenModel;

import java.util.Date;

public abstract class JwtTokenSerializer implements TokenSerializer {
    protected JWTClaimsSet getClaimsFromToken(TokenModel token) {
        return new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .subject(token.subject())
                .claim("authorities", token.authorities())
                .build();
    }
}
