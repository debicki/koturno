package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.InaccessibilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String serveHistoryPage(Model model) {
        List<Inaccessibility> allInaccessibilityList = inaccessibilityRepository.findAllByOrderByStartDesc();
        List<Inaccessibility> activeInaccessibilityList = new ArrayList<>();
        List<Inaccessibility> inactiveInaccessibilityList = new ArrayList<>();
        for (Inaccessibility inaccessibility : allInaccessibilityList) {
            if (inaccessibility.isActive()) {
                activeInaccessibilityList.add(inaccessibility);
            } else {
                inactiveInaccessibilityList.add(inaccessibility);
            }
        }
        model.addAttribute("activeInaccessibilityList", activeInaccessibilityList);
        model.addAttribute("inactiveInaccessibilityList", inactiveInaccessibilityList);
        model.addAttribute("disabledMenuItem", "history");
        return "/WEB-INF/views/history.jsp";
    }
}
