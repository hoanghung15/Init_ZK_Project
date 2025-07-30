package org.example.testdemozul.controller;

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
            String sessionId = jwtServiceImpl.generateToken(user, true, expiryTimeAccess);
            String refreshToken = jwtServiceImpl.generateToken(user, false, expiryTimeRefresh * 2);
            System.out.println(sessionId+" "+ refreshToken);
            Executions.sendRedirect("other.zul");
        }
    }
}
