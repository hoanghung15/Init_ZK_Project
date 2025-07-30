package org.example.testdemozul.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.testdemozul.model.User;
import org.example.testdemozul.service.interfaceClass.JwtService;

import java.util.Date;
import java.util.UUID;

public class JwtServiceImpl implements JwtService {
    private final String secretKey
            ="YfGpE7ID92/IVALUowbO1t7+P9CKBPrn2oHCcyRtHIKIYwrarPiwXC+hp/0sler1MZikzVw4iGmf4WgGKbgqLHgFsFmmWfKSeixGEKmUFmA=";

    @Override
    public String extractUsername(String token) {
        return "";
    }

    @Override
    public Date extractExpiration(String token) {
        return null;
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

    @Override
    public boolean validateToken(String token) {
        return false;
    }
}
