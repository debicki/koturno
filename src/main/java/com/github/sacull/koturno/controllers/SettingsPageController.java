package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/settings")
public class SettingsPageController {

    private final UserService userService;

    @Autowired
    public SettingsPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String serveSettingsPage(Model model,
                                    Principal principal) {

        model.addAttribute("firstUser", userService.countUsers() == 0);

        if (principal != null) {
            model.addAttribute("loggedUser", principal.getName());
        } else {
            model.addAttribute("loggedUser", null);
        }

        return "/WEB-INF/views/settings.jsp";
    }
}
