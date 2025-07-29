package org.example.testdemozul.controller;

import org.example.testdemozul.dao.UserDAO;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;

public class LoginController extends SelectorComposer<Component> {
    @Wire
    private Textbox username;

    @Wire
    private Textbox password;

    private UserDAO userDAO = new UserDAO();

    @Listen("onclick=#btnLogin")
    public void doLogin() {
        String username = this.username.getValue();
        String password = this.password.getValue();
    }
}
