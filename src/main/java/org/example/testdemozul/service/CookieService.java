package org.example.testdemozul.service;

import org.zkoss.zk.ui.Executions;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieService {
    public String getTokenFromCookie() {
        HttpServletRequest rq = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
        String token = null;
        if (rq.getCookies() != null) {
            for (Cookie cookie : rq.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    System.out.println("this is token: " + token);
                    break;
                }
            }
        }
        return token;
    }
}
