package org.example.staff_module.base.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.staff_module.base.config.custom.CustomUserDetail;
import org.example.staff_module.base.entity.Staff;
import org.example.staff_module.base.repository.StaffRepo;
import org.example.staff_module.base.service.TokenService;
import org.example.staff_module.base.service.inf.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtFilter extends OncePerRequestFilter {
    JwtService jwtService;
    StaffRepo staffRepo;
    TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null) {
            String accessToken = null;
            String username = null;
            if (header.startsWith("Bearer ")) {
                accessToken = header.substring(7);
                username = jwtService.extractUsername(accessToken);
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Staff staff = staffRepo.findByUsername(username);
                UserDetails userDetails = new CustomUserDetail(staff);

                List<String> listTokenStored = tokenService.getTokenFromRedis(username);
                if (listTokenStored != null && listTokenStored.contains(accessToken) && jwtService.validateToken(accessToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
