package com.sorokovsky.sorokchat.configuration;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.sorokovsky.sorokchat.deserializer.JweTokenDeserializer;
import com.sorokovsky.sorokchat.deserializer.JwsTokenDeserializer;
import com.sorokovsky.sorokchat.factory.AccessTokenFactory;
import com.sorokovsky.sorokchat.factory.DefaultAccessTokenFactory;
import com.sorokovsky.sorokchat.factory.DefaultRefreshTokenFactory;
import com.sorokovsky.sorokchat.factory.RefreshTokenFactory;
import com.sorokovsky.sorokchat.serializer.JweTokenSerializer;
import com.sorokovsky.sorokchat.serializer.JwsTokenSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.time.Duration;

@Configuration
public class TokensConfiguration {
    @Bean
    public JwsTokenSerializer jwsTokenSerializer(@Value("${tokens.access.secret-key}") String secretKey)
            throws ParseException, KeyLengthException {
        return JwsTokenSerializer
                .builder()
                .signer(new MACSigner(OctetSequenceKey.parse(secretKey)))
                .algorithm(JWSAlgorithm.HS256)
                .build();
    }

    @Bean
    public JwsTokenDeserializer jwsTokenDeserializer(@Value("${tokens.access.secret-key}") String secretKey)
            throws ParseException, JOSEException {
        return JwsTokenDeserializer
                .builder()
                .verifier(new MACVerifier(OctetSequenceKey.parse(secretKey)))
                .build();
    }

    @Bean
    public JweTokenSerializer jweTokenSerializer(@Value("${tokens.refresh.secret-key}") String secretKey)
            throws ParseException, KeyLengthException {
        return JweTokenSerializer
                .builder()
                .algorithm(JWEAlgorithm.DIR)
                .method(EncryptionMethod.A192GCM)
                .encrypter(new DirectEncrypter(OctetSequenceKey.parse(secretKey)))
                .build();
    }

    @Bean
    public JweTokenDeserializer jweTokenDeserializer(@Value("${tokens.refresh.secret-key}") String secretKey)
            throws ParseException, KeyLengthException {
        return JweTokenDeserializer
                .builder()
                .decrypter(new DirectDecrypter(OctetSequenceKey.parse(secretKey)))
                .build();
    }

    @Bean
    public RefreshTokenFactory refreshTokenFactory(@Value("${tokens.refresh.lifetime}") Duration lifetime) {
        return DefaultRefreshTokenFactory.builder().lifetime(lifetime).build();
    }

    @Bean
    public AccessTokenFactory accessTokenFactory(@Value("${tokens.access.lifetime}") Duration lifetime) {
        return DefaultAccessTokenFactory.builder().lifetime(lifetime).build();
    }
}
