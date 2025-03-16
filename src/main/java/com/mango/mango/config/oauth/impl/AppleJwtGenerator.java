package com.mango.mango.config.oauth.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Date;

@Component
public class AppleJwtGenerator {

    @Value("${OAUTH_APPLE_KEY_ID}")
    private static String keyId;

    @Value("${OAUTH_APPLE_TEAM_ID}")
    private static String teamId;

    @Value("${OAUTH_APPLE_CLIENT_ID}")
    private static String clientId;

    public static String generateClientSecret() throws Exception {

        byte[] keyBytes = Files.readAllBytes(Paths.get("AuthKey_C25DWL797J.p8"));
        ECPrivateKey privateKey = (ECPrivateKey) KeyFactory.getInstance("EC")
                .generatePrivate(new PKCS8EncodedKeySpec(keyBytes));

        Algorithm algorithm = Algorithm.ECDSA256(null, privateKey);

        return JWT.create()
                .withKeyId(keyId)
            .withIssuer(teamId)
            .withIssuedAt(Date.from(Instant.now()))
            .withExpiresAt(Date.from(Instant.now().plusSeconds(15777000)))
            .withAudience("https://appleid.apple.com")
            .withSubject(clientId)
            .sign(algorithm);
    }
}