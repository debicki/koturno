package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterPageController {

    private UserService userService;

    @Autowired
    public RegisterPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegisterPage() {
        return "/WEB-INF/views/register.jsp";
    }

    @PostMapping
    public String createUser(RedirectAttributes redirectAttributes,
                             String username,
                             String password,
                             String password2) {
        if (userService.findByName(username) != null) {
            redirectAttributes.addFlashAttribute("error", "31");
            return "redirect:/register";
        } else if (!password.equals(password2)) {
            redirectAttributes.addFlashAttribute("error", "32");
            return "redirect:/register";
        } else {
            userService.registerUser(username, password, true, "ROLE_USER");
            return "redirect:/login";
        }
    }
}
