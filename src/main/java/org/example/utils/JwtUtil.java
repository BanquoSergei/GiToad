package org.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Base64;
import java.util.Date;

public class JwtUtil {

    private final byte[] secret;

    public JwtUtil(String secret) {
        this.secret = secret.getBytes();
    }

    private Claims getClaimsFromToken(String authToken) {

        return Jwts.parserBuilder().setSigningKey(secret).build()
                .parseClaimsJws(authToken).getBody();
    }

    public boolean validateToken(String authToken) {

        return authToken != null
                && getClaimsFromToken(authToken)
                .getExpiration().before(new Date());
    }

    public String extractId(String token) {
        return getClaimsFromToken(token).get("id").toString();
    }
}
