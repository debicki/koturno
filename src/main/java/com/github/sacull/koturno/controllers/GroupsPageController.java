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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/groups")
public class GroupsPageController {

    private final HGroupRepository hGroupRepository;
    private final HostRepository hostRepository;

    @Autowired
    public GroupsPageController(HGroupRepository hGroupRepository, HostRepository hostRepository) {
        this.hGroupRepository = hGroupRepository;
        this.hostRepository = hostRepository;
    }

    @GetMapping
    public String serveGroupsPage(Model model) {
        List<HGroup> hGroups = hGroupRepository.findAll();
        List<Host> allHosts = hostRepository.findAll();
        Map<String, Integer> hGroupMembersCounter = new HashMap<>();
        for (HGroup hGroup : hGroups) {
            hGroupMembersCounter.put(hGroup.getName(), 0);
        }
        for (Host host : allHosts) {
            hGroupMembersCounter.replace(host.getHostGroup().getName(),
                    hGroupMembersCounter.get(host.getHostGroup().getName()) + 1);
        }
        model.addAttribute("groups", hGroups);
        model.addAttribute("groupMembersCounter", hGroupMembersCounter);
        model.addAttribute("disabledMenuItem", "groups");
        return "/WEB-INF/views/groups.jsp";
    }

    @PostMapping
    public String addNewHost(RedirectAttributes redirectAttributes,
                             String name,
                             String description) {
        if (description == null) {
            description = "";
        }
        if (hGroupRepository.findByName(name) == null) {
            HGroup hGroupToAdd = new HGroup(name, description);
            hGroupRepository.save(hGroupToAdd);
            redirectAttributes.addFlashAttribute("error", "0");
        } else {
            redirectAttributes.addFlashAttribute("error", "2");
        }
        return "redirect:/groups";
    }
}
