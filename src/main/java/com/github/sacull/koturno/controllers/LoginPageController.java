package com.github.sacull.koturno.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginPageController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "/WEB-INF/views/login.jsp";
    }

    @GetMapping("/logout")
    public String showPageAfterLogout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }
}
