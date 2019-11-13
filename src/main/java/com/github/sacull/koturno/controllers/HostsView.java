package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.HGroupRepository;
import com.github.sacull.koturno.repositories.HostRepository;
import com.github.sacull.koturno.repositories.InaccessibilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class HostsView {

    @Autowired
    private EntityManager em;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private InaccessibilityRepository inaccessibilityRepository;

    @Autowired
    private HGroupRepository hGroupRepository;

    private Logger logger = LoggerFactory.getLogger("HostsView");
//
//    @GetMapping("/inaccessibility/edit/{id}")
//    public String editInaccessibility(Model model, @PathVariable String id) {
//        Inaccessibility inaccessibility = inaccessibilityRepository.getById(Long.valueOf(id));
//        model.addAttribute("inaccessibility", inaccessibility);
//        return "iedit";
//    }
//
//    @PostMapping("/inaccessibility/update/{id}")
//    public String updateInaccessibility(Model model,
//                                        @PathVariable String id,
//                                        @Valid Inaccessibility inaccessibility) {
//        Inaccessibility inaccessibilityToUpdate = inaccessibilityRepository.getById(Long.parseLong(id));
//        inaccessibilityToUpdate.setDescription(inaccessibility.getDescription());
//        inaccessibilityRepository.save(inaccessibilityToUpdate);
//        logger.info("Inaccessability {} was updated", inaccessibilityToUpdate.getId());
//        return showDashboard(model);
//    }
}
