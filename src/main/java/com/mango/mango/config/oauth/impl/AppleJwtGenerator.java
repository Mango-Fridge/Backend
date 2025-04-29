package com.mango.mango.config.oauth.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.util.Base64;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.interfaces.ECPrivateKey;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.InputStream;
import java.time.Instant;
import java.util.Date;

@Component
public class AppleJwtGenerator {

    @Value("${OAUTH_APPLE_KEY_ID}")
    private String keyId;

    @Value("${OAUTH_APPLE_TEAM_ID}")
    private String teamId;

    @Value("${OAUTH_APPLE_CLIENT_ID}")
    private String clientId;

    public String generateClientSecret() throws Exception {
        ClassPathResource resource = new ClassPathResource("AuthKey_C25DWL797J.p8");
        InputStream inputStream = resource.getInputStream();
        String privateKeyPem = new String(inputStream.readAllBytes());
    
        privateKeyPem = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
    
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);
    
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