package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.services.InaccessibilityService;
import com.github.sacull.koturno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/inaccessibility")
public class InaccessibilityPageController {

    private final InaccessibilityService inaccessibilityService;
    private final UserService userService;

    @Autowired
    public InaccessibilityPageController(InaccessibilityService inaccessibilityService,
                                         UserService userService) {
        this.inaccessibilityService = inaccessibilityService;
        this.userService = userService;
    }

    @GetMapping
    public String doSomethingWithInaccessibility(Model model,
                                                 Principal principal,
                                                 Long id,
                                                 String action,
                                                 String filter) {

        model.addAttribute("firstUser", userService.countUsers() == 0);

        if (principal != null) {
            model.addAttribute("loggedUser", principal.getName());
        } else {
            model.addAttribute("loggedUser", null);
        }

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
    public String editInaccessibilityDescription(RedirectAttributes redirectAttributes,
                                                 Long id,
                                                 String description) {

        Inaccessibility inaccessibilityToSave = inaccessibilityService.getInaccessibilityById(id);
        inaccessibilityToSave.setDescription(description);
        inaccessibilityService.save(inaccessibilityToSave);

        redirectAttributes.addFlashAttribute("error", "0");
        return "redirect:/inaccessibility?id=" + inaccessibilityToSave.getId() + "&action=info";
    }
}
