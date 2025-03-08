package com.soft.mailinglist.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JWTUtill {
    @Value("${jwt_secret}")
    private String secret;

    public String generateAccessToken(String username) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusSeconds(10).toInstant());
        return JWT.create()
                .withSubject("USER TOKEN")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer("ROCKEZ")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String generateRefreshToken(String username) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusDays(7).toInstant());
        return JWT.create()
                .withSubject("REFRESH TOKEN")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer("ROCKEZ")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public Map<String, String> validateToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("ROCKEZ")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        Map<String, String> claims = new HashMap<>();
        jwt.getClaims().forEach((key, value) -> claims.put(key, value.asString()));

        return claims;
    }
}
