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
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/history")
public class HistoryPageController {

    private InaccessibilityService inaccessibilityService;
    private UserService userService;

    @Autowired
    public HistoryPageController(InaccessibilityService inaccessibilityService, UserService userService) {
        this.inaccessibilityService = inaccessibilityService;
        this.userService = userService;
    }

    @GetMapping
    public String serveHistoryPage(Model model,
                                   Principal principal,
                                   @RequestParam(required = false, defaultValue = "25") Integer limit,
                                   @RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "5") Integer range) {
        User loggedUser = userService.findByName(principal.getName());
        List<Inaccessibility> allInaccessibilityList = inaccessibilityService.findAllByOrderByStartDesc(loggedUser);
        List<Inaccessibility> limitedInaccessibilityList = new ArrayList<>();
        List<Inaccessibility> activeInaccessibilityList = new ArrayList<>();
        List<Inaccessibility> inactiveInaccessibilityList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < allInaccessibilityList.size(); i++) {
            Inaccessibility inaccessibility = allInaccessibilityList.get(i);
            if (inaccessibility.isActive()) {
                activeInaccessibilityList.add(inaccessibility);
            } else {
                if (Duration
                        .between(inaccessibility.getStart(), inaccessibility.getEnd())
                        .toMinutes() >= Long.valueOf(range)) {
                    inactiveInaccessibilityList.add(inaccessibility);
                }
            }
        }
        for (int i = 0; i < limit && i < activeInaccessibilityList.size(); i++) {
            limitedInaccessibilityList.add(activeInaccessibilityList.get(i));
        }
        if (limitedInaccessibilityList.size() < limit) {
            for (int i = 0; i < limit && i < inactiveInaccessibilityList.size(); i++) {
                limitedInaccessibilityList.add(inactiveInaccessibilityList.get(i));
            }
        }
        Integer numberOfPages = (allInaccessibilityList.size() - 1) / limit + 1;
        model.addAttribute("limit", limit);
        model.addAttribute("range", range);
        model.addAttribute("page", page);
        model.addAttribute("numberOfPages", numberOfPages);
        model.addAttribute("limitedInaccessibilityList", limitedInaccessibilityList);
        model.addAttribute("disabledMenuItem", "history");
        return "/WEB-INF/views/history.jsp";
    }
}
