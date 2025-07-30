package org.example.testdemozul.service.interfaceClass;

import io.jsonwebtoken.Claims;
import org.example.testdemozul.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);
    Date extractExpiration(String token);

    String generateToken(User user, boolean isAccess, int exp);
    boolean validateToken(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
}
