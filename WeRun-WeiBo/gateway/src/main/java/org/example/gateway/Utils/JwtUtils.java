package org.example.gateway.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtUtils {
    private static final String SECRET_KEY = "f4ds545f6a95s48a4s68d4a96w4fqs123z84g8ea4s486dg4t86j48u6g4k6l6sfg48es6hj4";
    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}