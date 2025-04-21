package com.example.loginLab.demo.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Utils {

    public static void clearCookie(HttpServletResponse response, String cookieName) {

        // Clear the cookie by setting a response header
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setSecure(false);

        response.addCookie(cookie);
    }


    public static String getCookie(HttpServletRequest request, String cookieName) {

        // static method is fine because it does not depend on any Spring bean
        String value = "";
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    value = cookie.getValue();
                    break;
                }
            }
        }

        return value;
    }
}
