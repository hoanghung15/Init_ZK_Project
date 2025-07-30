package org.example.testdemozul.service.interfaceClass;

import org.example.testdemozul.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {
    String extractUsername(String token);
    Date extractExpiration(String token);

    String generateToken(User user, boolean isAccess, int exp);
    boolean validateToken(String token);

}
