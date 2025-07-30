package org.example.testdemozul.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


import org.example.testdemozul.dao.UserDAO;
import org.example.testdemozul.model.User;
import org.example.testdemozul.service.JwtServiceImpl;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;

public class LoginController extends SelectorComposer<Component> {
    @Wire
    private Textbox username;

    @Wire
    private Textbox password;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        // Chặn cache trình duyệt
        HttpServletResponse response = (HttpServletResponse) Executions.getCurrent().getNativeResponse();
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // Proxies

        // Nếu đã có token thì redirect sang trang khác
        var request = (javax.servlet.http.HttpServletRequest) Executions.getCurrent().getNativeRequest();
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName()) && cookie.getValue() != null) {
                    // Có token -> đã login -> redirect
                    Executions.sendRedirect("other.zul");
                    return;
                }
            }
        }
    }

    private final int expiryTimeAccess = 3600;
    private final int expiryTimeRefresh = 7200;


    private UserDAO userDAO = new UserDAO();
    private JwtServiceImpl jwtServiceImpl = new JwtServiceImpl();

    @Listen("onClick=#btnLogin")
    public void doLogin() {
        String username = this.username.getValue();
        String password = this.password.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            throw new RuntimeException("Username and password are empty");
        }

        if (userDAO.checkLogin(username, password)) {
            User user = userDAO.getUser(username);
            Clients.showNotification("Logged in successfully");
            // Tạo JWT
            String accessToken = jwtServiceImpl.generateToken(user, true, expiryTimeAccess);
            String refreshToken = jwtServiceImpl.generateToken(user, false, expiryTimeRefresh * 2);

            System.out.println(accessToken + " " + refreshToken);

            // Lấy HttpServletResponse từ ZK Executions
            HttpServletResponse response = (HttpServletResponse) Executions.getCurrent().getNativeResponse();

            // Tạo cookie cho Access Token
            Cookie accessCookie = new Cookie("access_token", accessToken);
            accessCookie.setHttpOnly(true);
            accessCookie.setPath("/");
            accessCookie.setMaxAge(expiryTimeAccess);

            // Tạo cookie cho Refresh Token
            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(expiryTimeRefresh * 2);

            // Thêm cookie vào response
            response.addCookie(accessCookie);
            response.addCookie(refreshCookie);

            Executions.sendRedirect("other.zul");
        } else {
            Clients.showNotification("Sai username hoặc password", "error", null, "top_center", 3000);
        }
    }
}
