package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginPageController {

    private final UserService userService;

    @Autowired
    public LoginPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("firstUser", userService.countUsers() == 0);
        return "/WEB-INF/views/login.jsp";
    }

    @GetMapping("/logout")
    public String showPageAfterLogout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }
}
