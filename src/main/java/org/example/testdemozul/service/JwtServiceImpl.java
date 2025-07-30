package org.example.testdemozul.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.testdemozul.model.User;
import org.example.testdemozul.service.interfaceClass.JwtService;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

public class JwtServiceImpl implements JwtService {
    private final String secretKey
            = "YfGpE7ID92/IVALUowbO1t7+P9CKBPrn2oHCcyRtHIKIYwrarPiwXC+hp/0sler1MZikzVw4iGmf4WgGKbgqLHgFsFmmWfKSeixGEKmUFmA=";

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey(secretKey))
                .build()
                .parseClaimsJws(token).getBody();
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public String generateToken(User user, boolean isAccess, int exp) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        var key = Keys.hmacShaKeyFor(keyBytes);

        var builder = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString())
                .setExpiration(new Date(System.currentTimeMillis() + exp * 1000));

        if (isAccess) builder.claim("scope", "admin");

        return builder.signWith(key, SignatureAlgorithm.HS512).compact();
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    @Override
    public boolean validateToken(String token) {
        final String username = extractUsername(token);
        return ( !isTokenExpired(token));
    }

    private Key getSignKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static void main(String[] args) {
        JwtServiceImpl jwtServiceImpl = new JwtServiceImpl();
        System.out.println(jwtServiceImpl.extractAllClaims("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2h1bmcxNSIsImlhdCI6MTc1Mzg0NDI3MywianRpIjoiZTY2ZTI3MDQtYmEwMi00ZmRhLWE3ODMtNGIwYmQwYTE5ODJkIiwiZXhwIjoxNzUzODQ3ODczLCJzY29wZSI6ImFkbWluIn0.g0dSZOPnrERQM2rampP_1hni1uX0s-dOOtxI8JlkpJ5kfjS7dtkU_1siPFKegWuMq6mxwf-p_urcOIvkUmO1kA"));
        System.out.println(jwtServiceImpl.extractUsername("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2h1bmcxNSIsImlhdCI6MTc1Mzg0NDI3MywianRpIjoiZTY2ZTI3MDQtYmEwMi00ZmRhLWE3ODMtNGIwYmQwYTE5ODJkIiwiZXhwIjoxNzUzODQ3ODczLCJzY29wZSI6ImFkbWluIn0.g0dSZOPnrERQM2rampP_1hni1uX0s-dOOtxI8JlkpJ5kfjS7dtkU_1siPFKegWuMq6mxwf-p_urcOIvkUmO1kA"));
        System.out.println(jwtServiceImpl.extractExpiration("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2h1bmcxNSIsImlhdCI6MTc1Mzg0NDI3MywianRpIjoiZTY2ZTI3MDQtYmEwMi00ZmRhLWE3ODMtNGIwYmQwYTE5ODJkIiwiZXhwIjoxNzUzODQ3ODczLCJzY29wZSI6ImFkbWluIn0.g0dSZOPnrERQM2rampP_1hni1uX0s-dOOtxI8JlkpJ5kfjS7dtkU_1siPFKegWuMq6mxwf-p_urcOIvkUmO1kA"));
    }
}
