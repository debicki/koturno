package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.repositories.HGroupRepository;
import com.github.sacull.koturno.repositories.HostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/group")
public class GroupController {

    HGroupRepository hGroupRepository;
    HostRepository hostRepository;

    @Autowired
    public GroupController(HGroupRepository hGroupRepository, HostRepository hostRepository) {
        this.hGroupRepository = hGroupRepository;
        this.hostRepository = hostRepository;
    }

    @GetMapping
    public String doSomethingWithGroup(Model model,
                                       Long id,
                                       String action) {
        HGroup hGroup = hGroupRepository.getOne(id);
        List<Host> groupHosts = hostRepository.findAllByHostGroup(hGroup);
        model.addAttribute("group", hGroup);
        model.addAttribute("hosts", groupHosts);
        return "/WEB-INF/views/group.jsp";
    }
}
