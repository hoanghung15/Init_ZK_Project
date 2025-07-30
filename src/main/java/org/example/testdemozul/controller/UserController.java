package org.example.testdemozul.controller;

import org.example.testdemozul.dao.UserDAO;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;

public class UserController extends SelectorComposer<Component> {
    @Wire
    private Label username;

    private UserDAO userDAO = new UserDAO();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    System.out.println(token);
                    break;
                }
            }
        }
    }

}

