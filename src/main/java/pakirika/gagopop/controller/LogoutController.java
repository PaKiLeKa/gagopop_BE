package pakirika.gagopop.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class LogoutController {

    @GetMapping("/api/logout")
    public void myLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Cookie[] cookies =request.getCookies();

        response.addCookie(deleteCookie("Authorization", ""));

        response.sendRedirect("/");
    }

    @GetMapping("/admin/logout")
    public String adminLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.addCookie(deleteCookie("Authorization", ""));

        return "admin/login";
    }

    private Cookie deleteCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute( "SameSite", "None" );
        cookie.setHttpOnly(true);

        return cookie;
    }
}
