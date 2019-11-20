package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.services.InaccessibilityService;
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

    @Autowired
    public DashboardPageController(InaccessibilityService inaccessibilityService) {
        this.inaccessibilityService = inaccessibilityService;
    }

    @GetMapping
    public String serveDashboardPage(Model model) {
        List<Inaccessibility> instabilityHosts =
                inaccessibilityService.findAllByActiveIsTrueOrderByStartDesc();
        model.addAttribute("instabilityHosts", instabilityHosts);
        model.addAttribute("disabledMenuItem", "dashboard");
        return "/WEB-INF/views/dashboard.jsp";
    }
}
