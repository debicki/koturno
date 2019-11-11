package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.HostRepository;
import com.github.sacull.koturno.repositories.InaccessibilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/host")
public class HostController {

    private final InaccessibilityRepository inaccessibilityRepository;
    private final HostRepository hostRepository;

    @Autowired
    public HostController(InaccessibilityRepository inaccessibilityRepository,
                          HostRepository hostRepository) {
        this.inaccessibilityRepository = inaccessibilityRepository;
        this.hostRepository = hostRepository;
    }

    @GetMapping
    public String doSomethingWithHost(Model model,
                                      Long id,
                                      String action) {
        Host host = hostRepository.getOne(id);
        List<Inaccessibility> hostInaccessibilityList = inaccessibilityRepository.findAllByHostOrderByStartDesc(host);
        model.addAttribute("host", host);
        model.addAttribute("inaccessibilityList", hostInaccessibilityList);
        return "/WEB-INF/views/host.jsp";
    }
}
