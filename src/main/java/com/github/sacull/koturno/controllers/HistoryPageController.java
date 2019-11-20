package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.services.InaccessibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/history")
public class HistoryPageController {

    private InaccessibilityService inaccessibilityService;

    @Autowired
    public HistoryPageController(InaccessibilityService inaccessibilityService) {
        this.inaccessibilityService = inaccessibilityService;
    }

    @GetMapping
    public String serveHistoryPage(Model model,
                                   @RequestParam(required = false, defaultValue = "only-offline") String filter,
                                   @RequestParam(required = false, defaultValue = "100") Integer limit) {
        List<Inaccessibility> allInaccessibilityList = inaccessibilityService.findAllByOrderByStartDesc();
        List<Inaccessibility> limitedInaccessibilityList = new ArrayList<>();
        List<Inaccessibility> activeInaccessibilityList = new ArrayList<>();
        List<Inaccessibility> inactiveInaccessibilityList = new ArrayList<>();
        for (Inaccessibility inaccessibility : allInaccessibilityList) {
            if (inaccessibility.isActive()) {
                activeInaccessibilityList.add(inaccessibility);
            } else {
                if (filter.equalsIgnoreCase("only-offline")) {
                    if (inaccessibility.isOfflineStatus()) {
                        inactiveInaccessibilityList.add(inaccessibility);
                    }
                    filter = "only-offline";
                } else if (filter.equalsIgnoreCase("no-ignored")) {
                    if (!inaccessibility.getStart().equals(inaccessibility.getEnd())) {
                        inactiveInaccessibilityList.add(inaccessibility);
                    }
                    filter = "no-ignored";
                } else {
                    inactiveInaccessibilityList.add(inaccessibility);
                    filter = "all";
                }
            }
        }
        for (int i = 0; i < limit && i < activeInaccessibilityList.size(); i++) {
            limitedInaccessibilityList.add(activeInaccessibilityList.get(i));
        }
        if (limitedInaccessibilityList.size() < limit) {
            for (int i = 0; i < limit && i < inactiveInaccessibilityList.size(); i++) {
                limitedInaccessibilityList.add(inactiveInaccessibilityList .get(i));
            }
        }
        model.addAttribute("filter", filter);
        model.addAttribute("limit", limit);
        model.addAttribute("limitedInaccessibilityList", limitedInaccessibilityList);
        model.addAttribute("disabledMenuItem", "history");
        return "/WEB-INF/views/history.jsp";
    }
}
