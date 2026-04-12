package com.example.drtraker.demo.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtil {
	private final String SECRET = "mysecretkeymysecretkeymysecretkey";

    private final long ACCESS_EXP = 1000 * 60 * 15;
    private final long REFRESH_EXP = 1000 * 60 * 60 * 24;

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP))
                .signWith(getKey())
                .compact();	
    }
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .signWith(getKey())
                .compact();
    }
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
 
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
