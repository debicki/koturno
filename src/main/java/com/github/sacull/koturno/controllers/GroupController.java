package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.repositories.HGroupRepository;
import com.github.sacull.koturno.repositories.HostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping
    public String editGroup(RedirectAttributes redirectAttributes,
                           String originName,
                           String name,
                           String description) {
        HGroup groupToSave = hGroupRepository.findByName(originName);
        if (originName.equalsIgnoreCase("default")) {
            redirectAttributes.addFlashAttribute("error", "3");
        } else if (hGroupRepository.findByName(name) == null || name.equalsIgnoreCase(name)) {
            groupToSave.setName(name);
            groupToSave.setDescription(description);
            hGroupRepository.save(groupToSave);
            redirectAttributes.addFlashAttribute("error", "0");
        } else {
            redirectAttributes.addFlashAttribute("error", "2");
        }
        return "redirect:/group?id=" + groupToSave.getId() +"&action=info";
    }
}
