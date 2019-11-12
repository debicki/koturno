package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.InaccessibilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inaccessibility")
public class InaccessibilityController {

    InaccessibilityRepository inaccessibilityRepository;

    @Autowired
    public InaccessibilityController(InaccessibilityRepository inaccessibilityRepository) {
        this.inaccessibilityRepository = inaccessibilityRepository;
    }

    @GetMapping
    public String doSomethingWithInaccessibility(Model model,
                                                 Long id,
                                                 String action) {
        Inaccessibility inaccessibility = inaccessibilityRepository.getOne(id);
        if (action.equals("ignore")) {
            inaccessibility.setActive(false);
            inaccessibilityRepository.save(inaccessibility);
        } else if (action.equals("remove")) {
            inaccessibilityRepository.delete(inaccessibility);
            return "redirect:/history";
        } else if (action.equals("info")) {
            model.addAttribute("inaccessibility", inaccessibility);
            return "/WEB-INF/views/inaccessibility.jsp";
        }
        return "redirect:/";
    }

    @PostMapping
    public String editHost(Long id, String description) {
        Inaccessibility inaccessibilityToSave = inaccessibilityRepository.getOne(id);
        inaccessibilityToSave.setDescription(description);
        inaccessibilityRepository.save(inaccessibilityToSave);
        return "redirect:/inaccessibility?id=" + inaccessibilityToSave.getId() +"&action=info";
    }
}
