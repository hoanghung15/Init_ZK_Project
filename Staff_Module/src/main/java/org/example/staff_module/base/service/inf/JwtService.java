package org.example.staff_module.base.service.inf;

import io.jsonwebtoken.Claims;;
import org.example.staff_module.base.entity.Staff;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;


public interface JwtService {
    String extractUsername(String token);
    Date extractExpiration(String token);

    String generateToken(Staff staff, boolean isAccess, int exp);
    boolean validateToken(String token, UserDetails userDetails);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

}