package org.example.auth.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtils {
    //一个非常长的密钥
    private static final String SECRET_KEY = "Weiboyour_secret_keyWeiboyour_secret_keyWeiboyour_secret_keyWeiboyour_secret_keyWeiboyour_secret_key";
    //令牌有效的时长
    private static final long EXPIRATION = 3600; // 1小时
//生成JWT令牌
    public static String generateToken(Integer id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

}
