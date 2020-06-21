package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.dtos.UserDto;
import com.github.sacull.koturno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UsersPageController {

    private final UserService userService;

    @Autowired
    public UsersPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String serveUsersPage(Model model,
                                 RedirectAttributes redirectAttributes,
                                 Principal principal,
                                 @RequestParam(required = false) String username,
                                 @RequestParam(required = false) String action) {

        model.addAttribute("firstUser", userService.countUsers() == 0);

        if (principal != null) {
            model.addAttribute("loggedUser", principal.getName());
        } else {
            model.addAttribute("loggedUser", null);
        }

        if (action != null) {
            UserDto userToCheck = userService.findByUsername(username);
            if (action.equalsIgnoreCase("enable")) {
                userService.enableUser(username);
                redirectAttributes.addFlashAttribute("error", "user-enabled");
            } else if (action.equalsIgnoreCase("disable")) {
                if (userToCheck.getRole().equalsIgnoreCase("role_admin")
                        && userService.countActiveAdmins() <= 1) {
                    redirectAttributes.addFlashAttribute("error", "no-active-admin");
                } else {
                    userService.disableUser(username);
                    redirectAttributes.addFlashAttribute("error", "user-disabled");
                }
            } else if (action.equalsIgnoreCase("remove")) {
                if (userToCheck.getRole().equalsIgnoreCase("role_admin")
                        && userService.countActiveAdmins() <= 1) {
                    redirectAttributes.addFlashAttribute("error", "no-active-admin");
                } else {
                    userService.removeUser(username);
                    redirectAttributes.addFlashAttribute("error", "user-removed");
                }
            }

            return "redirect:/users";
        }

        model.addAttribute("users", userService.findAllUsers());
        return "/WEB-INF/views/users.jsp";
    }

    @PostMapping("/add")
    public String createUser(RedirectAttributes redirectAttributes,
                             String username,
                             String password,
                             String password2,
                             String role,
                             String activity) {

        if (userService.findByName(username) != null) {
            redirectAttributes.addFlashAttribute("error", "user-exists");
            return "redirect:/users";
        } else if (!password.equals(password2)) {
            redirectAttributes.addFlashAttribute("error", "passwords-mismatch");
            return "redirect:/users";
        } else {
            userService.registerUser(username, password, Boolean.parseBoolean(activity), role);
            redirectAttributes.addFlashAttribute("error", "user-added");
            return "redirect:/users";
        }
    }

    @PostMapping("/edit")
    public String editUser(RedirectAttributes redirectAttributes,
                           String oldUsername,
                           String username,
                           String role,
                           String activity) {

        UserDto user = userService.findByUsername(oldUsername);
        if (userService.findByName(username) != null && !oldUsername.equals(username)) {
            redirectAttributes.addFlashAttribute("error", "user-exists");
        } else {
            userService.updateUser(user, username, Boolean.parseBoolean(activity), role);
            redirectAttributes.addFlashAttribute("error", "user-updated");
        }

        return "redirect:/users";
    }
}
