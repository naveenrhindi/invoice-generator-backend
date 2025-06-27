package com.naveen.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class ClerkJwksProvider {

    @Value("${clerk.jwks-url}")
    private String jwksUrl;

    private final Map<String, PublicKey> keyCache = new HashMap<>();
    private long lastFetchTime = 0;
    private static final long CACHE_TTL = 3600000;

    public PublicKey getPublicKey(String kid) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if(keyCache.containsKey(kid) && System.currentTimeMillis() - lastFetchTime < CACHE_TTL) {
            return keyCache.get(kid);
        }
        refreshKeys();
        return keyCache.get(kid);
    }

    private void refreshKeys() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jwks = mapper.readTree(new URL(jwksUrl));
        JsonNode keys = jwks.get("keys");
        for (JsonNode node : keys) {
            String kid = node.get("kid").asText(); // keyId
            String kty = node.get("kty").asText(); // keyType
            String alg = node.get("alg").asText(); // algorithm

            if("RSA".equals(kty) && "RS256".equals(alg)) {
                String n = node.get("n").asText(); // modulus
                String e = node.get("e").asText(); // exponent

                PublicKey publicKey = createPublicKey(n, e);
                keyCache.put(kid, publicKey);
            }
        }
        lastFetchTime = System.currentTimeMillis();
    }

    private PublicKey createPublicKey(String modulus, String exponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] modulusBytes = Base64.getUrlDecoder().decode(modulus);
        byte[] exponentBytes = Base64.getUrlDecoder().decode(exponent);

        BigInteger modulusBigInt = new BigInteger(1, modulusBytes);
        BigInteger exponentBigInt = new BigInteger(1, exponentBytes);

        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulusBigInt, exponentBigInt);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

}