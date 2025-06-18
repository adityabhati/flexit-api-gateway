package com.flexit.api_gateway.util;

import com.flexit.api_gateway.util.constants.IniConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.configuration.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static SecretKey secretKey;

    public JwtUtil(Configuration iniConfiguration) {
        secretKey = Keys.hmacShaKeyFor(iniConfiguration.getString(IniConstants.USER_PASS_ENCRYPTION_KEY).getBytes());
    }

    public static String extractToken(String authHeader) {
        return (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.substring(7)
                : null;
    }

    public static boolean validateToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(secretKey)
                    .build();

            Claims claims = parser.parseSignedClaims(token).getPayload();
            Date expiration = claims.getExpiration();
            return expiration == null || !expiration.before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    public static Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}