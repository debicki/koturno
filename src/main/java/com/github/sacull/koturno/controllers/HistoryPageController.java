package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.InaccessibilityRepository;
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

    private final InaccessibilityRepository inaccessibilityRepository;

    @Autowired
    public HistoryPageController(InaccessibilityRepository inaccessibilityRepository) {
        this.inaccessibilityRepository = inaccessibilityRepository;
    }

    @GetMapping
    public String serveHistoryPage(Model model,
                                   @RequestParam(required = false, defaultValue = "only-offline") String filter) {
        List<Inaccessibility> allInaccessibilityList = inaccessibilityRepository.findAllByOrderByStartDesc();
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
        model.addAttribute("filter", filter);
        model.addAttribute("activeInaccessibilityList", activeInaccessibilityList);
        model.addAttribute("inactiveInaccessibilityList", inactiveInaccessibilityList);
        model.addAttribute("disabledMenuItem", "history");
        return "/WEB-INF/views/history.jsp";
    }
}
