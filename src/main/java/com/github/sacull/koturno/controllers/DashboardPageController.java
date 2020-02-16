package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.entities.User;
import com.github.sacull.koturno.services.InaccessibilityService;
import com.github.sacull.koturno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class DashboardPageController {

    private InaccessibilityService inaccessibilityService;
    private UserService userService;

    @Autowired
    public DashboardPageController(InaccessibilityService inaccessibilityService,
                                   UserService userService) {

        this.inaccessibilityService = inaccessibilityService;
        this.userService = userService;
    }

    @GetMapping
    public String serveDashboardPage(Model model,
                                     Principal principal) {

        if (principal != null) {
            User loggedUser = userService.findByName(principal.getName());
            List<Inaccessibility> instabilityHosts =
                    inaccessibilityService.findAllByActiveIsTrueOrderByStartDesc();
            model.addAttribute("instabilityHosts", instabilityHosts);
        } else {
            model.addAttribute("instabilityHosts", new ArrayList<Inaccessibility>());
        }

        model.addAttribute("disabledMenuItem", "dashboard");
        return "/WEB-INF/views/dashboard.jsp";
    }
}
