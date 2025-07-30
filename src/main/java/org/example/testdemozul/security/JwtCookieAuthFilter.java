package org.example.testdemozul.security;

import org.example.testdemozul.service.JwtServiceImpl;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class JwtCookieAuthFilter implements Filter {

    private final JwtServiceImpl jwtServiceImpl = new JwtServiceImpl();

    @Override
    public void init(FilterConfig filterConfig) { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        String uri = httpReq.getRequestURI();

        // Bỏ qua file login hoặc resource tĩnh
        if (uri.endsWith("index.zul") || uri.contains("/zkau") || uri.contains("/static/")) {
            chain.doFilter(request, response);
            return;
        }

        // Lấy access_token từ cookie
        String token = null;
        if (httpReq.getCookies() != null) {
            for (Cookie cookie : httpReq.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // Kiểm tra token
        if (token == null || !jwtServiceImpl.validateToken(token)) {
            // Không hợp lệ -> chuyển về trang login
            httpRes.sendRedirect(httpReq.getContextPath() + "/index.zul");
            return;
        }

        // Hợp lệ -> cho phép tiếp tục
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() { }
}
