package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.InaccessibilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        model.addAttribute("inaccessibility", inaccessibility);
        return "/WEB-INF/views/inaccessibility.jsp";
    }
}
