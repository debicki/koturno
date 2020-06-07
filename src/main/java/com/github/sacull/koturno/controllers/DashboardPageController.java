package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.services.HostService;
import com.github.sacull.koturno.services.InaccessibilityService;
import com.github.sacull.koturno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
public class DashboardPageController {

    private HostService hostService;
    private InaccessibilityService inaccessibilityService;
    private UserService userService;

    @Autowired
    public DashboardPageController(HostService hostService,
                                   InaccessibilityService inaccessibilityService,
                                   UserService userService) {

        this.hostService = hostService;
        this.inaccessibilityService = inaccessibilityService;
        this.userService = userService;
    }

    @GetMapping
    public String serveDashboardPage(Model model, Principal principal) {

        if (userService.countUsers() == 0) {
            model.addAttribute("firstUser", true);
        } else {
            model.addAttribute("firstUser", false);
        }

        if (principal != null) {
            model.addAttribute("loggedUser", principal.getName());
        } else {
            model.addAttribute("loggedUser", null);
        }

        List<Inaccessibility> instabilityHosts =
                inaccessibilityService.findAllByActiveIsTrueOrderByStartDesc();
        model.addAttribute("instabilityHosts", instabilityHosts);

        Long inaccessibleHostsNumber = instabilityHosts.stream()
                .filter(Inaccessibility::isOfflineStatus)
                .count();
        model.addAttribute("inaccessibleHostsNumber", inaccessibleHostsNumber);

        Long unstableHostsNumber = instabilityHosts.stream()
                .filter(host -> !host.isOfflineStatus())
                .count();
        model.addAttribute("unstableHostsNumber", unstableHostsNumber);

        Long onlineHostsNumber = hostService.countAllActiveHost() - (inaccessibleHostsNumber + unstableHostsNumber);
        model.addAttribute("onlineHostsNumber", onlineHostsNumber);

        Long inactiveHostsNumber = hostService.countAllInactiveHost();
        model.addAttribute("inactiveHostsNumber", inactiveHostsNumber);

        return "/WEB-INF/views/dashboard.jsp";
    }
}
