package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.services.InaccessibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inaccessibility")
public class InaccessibilityController {

    private InaccessibilityService inaccessibilityService;

    @Autowired
    public InaccessibilityController(InaccessibilityService inaccessibilityService) {
        this.inaccessibilityService = inaccessibilityService;
    }

    @GetMapping
    public String doSomethingWithInaccessibility(Model model,
                                                 Long id,
                                                 String action,
                                                 String filter) {
        Inaccessibility inaccessibility = inaccessibilityService.getInaccessibilityById(id);
        switch (action) {
            case "ignore":
                inaccessibility.setActive(false);
                inaccessibilityService.save(inaccessibility);
                break;
            case "remove":
                inaccessibilityService.delete(inaccessibility);
                return "redirect:/history?filter=" + filter;
            case "info":
                model.addAttribute("inaccessibility", inaccessibility);
                return "/WEB-INF/views/inaccessibility.jsp";
        }
        return "redirect:/";
    }

    @PostMapping
    public String editHost(Long id, String description) {
        Inaccessibility inaccessibilityToSave = inaccessibilityService.getInaccessibilityById(id);
        inaccessibilityToSave.setDescription(description);
        inaccessibilityService.save(inaccessibilityToSave);
        return "redirect:/inaccessibility?id=" + inaccessibilityToSave.getId() +"&action=info";
    }
}
