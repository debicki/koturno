package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.services.InaccessibilityService;
import com.github.sacull.koturno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String serveDashboardPage(Model model) {

        if (userService.countUsers() == 0) {
            model.addAttribute("firstUser", true);
        }

        List<Inaccessibility> instabilityHosts =
                inaccessibilityService.findAllByActiveIsTrueOrderByStartDesc();
        model.addAttribute("instabilityHosts", instabilityHosts);

        model.addAttribute("disabledMenuItem", "dashboard");
        return "/WEB-INF/views/dashboard.jsp";
    }
}
