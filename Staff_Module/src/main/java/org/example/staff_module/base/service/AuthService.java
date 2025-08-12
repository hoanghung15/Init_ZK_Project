package org.example.staff_module.base.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.example.staff_module.base.entity.Staff;
import org.example.staff_module.base.repository.StaffRepo;
import org.example.staff_module.dto.request.LoginRequest;
import org.example.staff_module.dto.response.ApiResponse;
import org.example.staff_module.dto.response.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    @Value("${jwt.access.expire}")
    @NonFinal
    int accessExpire;

    @Value("${jwt.refresh.expire}")
    @NonFinal
    int refreshExpire;

    @NonFinal
    @Value("${jwt.access.expire}")
    int accessExpiration;

    @NonFinal
    @Value("${jwt.refresh.expire}")
    int refreshExpiration;

    StaffRepo staffRepo;
    PasswordEncoder passwordEncoder;
    JwtServiceImpl jwtServiceImpl;
    TokenService tokenService;

    public ApiResponse<AuthResponse> login(LoginRequest loginRequest) {
        boolean checkUsername = staffRepo.existsByUsername(loginRequest.getUsername());
        if (!checkUsername) {
            throw new RuntimeException("Login Infor not correct");
        }
        Staff staff = staffRepo.findByUsername((loginRequest.getUsername()));

        boolean checkPassword = passwordEncoder.matches(loginRequest.getPassword(), staff.getPassword());
        String accessToken = null;
        String refreshToken = null;
        if (checkPassword) {
            accessToken = jwtServiceImpl.generateToken(staff, true, accessExpire);
            refreshToken = jwtServiceImpl.generateToken(staff, false, refreshExpire);

            tokenService.saveTokenInRedis(staff.getUsername(), accessToken, accessExpiration);
            tokenService.saveTokenInRedis(staff.getUsername(), refreshToken, refreshExpiration);
            log.info("User's Token in Redis: {}", tokenService.getTokenFromRedis(staff.getUsername()));

            AuthResponse authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            return ApiResponse.<AuthResponse>builder()
                    .code(200)
                    .message("Success")
                    .result(authResponse)
                    .build();
        } else {
            throw new RuntimeException("Login info is not correct");
        }
    }


    public ApiResponse logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            String username = jwtServiceImpl.extractUsername(token);
            tokenService.deleteTokenFromRedis(username);
        }
        return ApiResponse.builder()
                .code(200)
                .message("Log-out successfully")
                .build();
    }
}
