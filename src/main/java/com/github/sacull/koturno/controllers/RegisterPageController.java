package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/register")
public class RegisterPageController {

    private UserService userService;

    @Autowired
    public RegisterPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegisterPage(Model model, Principal principal) {

        if (userService.countUsers() == 0) {
            model.addAttribute("firstUser", true);
        } else {
            model.addAttribute("firstUser", false);
        }

//        if (principal != null) {
//            model.addAttribute("loggedUser", principal.getName());
//        } else {
//            model.addAttribute("loggedUser", null);
//        }

        if (userService.countUsers() != 0 && principal == null) {
            return "403";
        } else {
            return "/WEB-INF/views/register.jsp";
        }
    }

    @PostMapping
    public String createUser(RedirectAttributes redirectAttributes,
                             String username,
                             String password,
                             String password2,
                             Principal principal) {

        if (userService.findByName(username) != null) {
            redirectAttributes.addFlashAttribute("error", "user-exists");
            return "redirect:/register";
        } else if (!password.equals(password2)) {
            redirectAttributes.addFlashAttribute("error", "passwords-mismatch");
            return "redirect:/register";
        } else {
            userService.registerUser(username, password, true, "ROLE_ADMIN");

            if (principal == null) {
                return "redirect:/login";
            } else {
                return "redirect:/";
            }
        }
    }
}
