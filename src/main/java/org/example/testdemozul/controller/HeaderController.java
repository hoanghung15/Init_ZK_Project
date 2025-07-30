package org.example.testdemozul.controller;

import org.example.testdemozul.dao.UserDAO;
import org.example.testdemozul.service.CookieService;
import org.example.testdemozul.service.JwtServiceImpl;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;

public class HeaderController extends SelectorComposer<Component> {
    @Wire
    private Label usernameTxt;

    private JwtServiceImpl jwtServiceImpl = new JwtServiceImpl();

    private UserDAO userDAO = new UserDAO();
    private CookieService cookieService = new CookieService();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        String token = cookieService.getTokenFromCookie();
        Selectors.wireComponents(comp, this, true);
        String name = jwtServiceImpl.extractUsername(token);
        usernameTxt.setValue("Xin chào, "+name);
    }
    @Listen("onClick=#btnLogout")
    public void doLogout() {
        HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
        HttpServletResponse response = (HttpServletResponse) Executions.getCurrent().getNativeResponse();

        // Lấy tất cả cookie và xóa
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }

        Executions.sendRedirect("index.zul");
    }

}