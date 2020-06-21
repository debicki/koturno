package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.dtos.UserDto;
import com.github.sacull.koturno.services.HostService;
import com.github.sacull.koturno.services.InaccessibilityService;
import com.github.sacull.koturno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/settings")
public class SettingsPageController {

    private final UserService userService;
    private final HostService hostService;
    private final InaccessibilityService inaccessibilityService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SettingsPageController(UserService userService,
                                  HostService hostService,
                                  InaccessibilityService inaccessibilityService,
                                  PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.hostService = hostService;
        this.inaccessibilityService = inaccessibilityService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String serveSettingsPage(Model model,
                                    Principal principal) {

        model.addAttribute("firstUser", userService.countUsers() == 0);

        if (principal != null) {
            model.addAttribute("loggedUser", principal.getName());
            UserDto user = userService.findByUsername(principal.getName());
            model.addAttribute("user", user);
        } else {
            model.addAttribute("loggedUser", null);
        }

        return "/WEB-INF/views/settings.jsp";
    }

    @PostMapping("/user/change-username")
    public String changeUsername(RedirectAttributes redirectAttributes,
                             Principal principal,
                             String newUsername,
                             String password) {

        if (principal == null) {
            return "redirect:/";
        } else {
            UserDto user = userService.findByUsername(principal.getName());
            if (userService.findByName(newUsername) != null) {
                redirectAttributes.addFlashAttribute("error", "username-occupied");
            } else if (!passwordEncoder.matches(password, user.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "wrong-password");
            } else {
                userService.updateUsername(user, newUsername);
            }
        }

        return "redirect:/logout";
    }

    @PostMapping("/user/change-password")
    public String changePassword(RedirectAttributes redirectAttributes,
                                 Principal principal,
                                 String oldPassword,
                                 String newPassword,
                                 String newPassword2) {

        if (principal == null) {
            return "redirect:/";
        } else {
            UserDto user = userService.findByUsername(principal.getName());
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "wrong-password");
            } else if (!newPassword.equals(newPassword2)) {
                redirectAttributes.addFlashAttribute("error", "passwords-mismatch");
            } else {
                userService.updatePassword(user, newPassword);
                redirectAttributes.addFlashAttribute("error", "passwords-changed");
            }
        }

        return "redirect:/settings";
    }

    @PostMapping("/db")
    public String removeFromDatabase(RedirectAttributes redirectAttributes,
                                 Principal principal,
                                 String action,
                                 String password) {

        if (principal != null) {
            UserDto user = userService.findByUsername(principal.getName());
            if (!user.getRole().equalsIgnoreCase("role_admin")) {
                return "redirect:/";
            } else if (!passwordEncoder.matches(password, user.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "wrong-password");
                return "redirect:/settings";
            }
        } else {
            return "redirect:/";
        }

        switch (action) {
            case "clear-history":
                inaccessibilityService.removeAll();
                break;
            case "clear-hosts":
                inaccessibilityService.removeAll();
                hostService.removeAll();
                break;
            case "clear-database":
                inaccessibilityService.removeAll();
                hostService.removeAll();
                userService.removeAll();
                return "redirect:/logout";
        }

        return "redirect:/settings";
    }
}
