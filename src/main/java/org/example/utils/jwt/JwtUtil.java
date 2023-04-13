package org.example.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public class JwtUtil {

    private byte[] secret;

    public JwtUtil(String secret) {
        this.secret = secret.getBytes();
    }

    private Claims getClaimsFromToken(String authToken) {

        return Jwts.parserBuilder().setSigningKey(secret).build()
                .parseClaimsJws(authToken).getBody();
    }

    public boolean validateToken(String token) {

        if(token == null || token.length() <= 7)
            return false;

        token = token.substring(7);

        return getClaimsFromToken(token)
                .getExpiration().after(new Date());
    }

    public String extractId(String token) {

        if(token == null || token.length() <= 7)
            throw new IllegalArgumentException("Token must be longer");

        token = token.substring(7);

        return getClaimsFromToken(token).get("id").toString();
    }

    public void setKey(byte[] key) {
        secret = key;
    }
}
