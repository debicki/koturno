package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.services.HGroupService;
import com.github.sacull.koturno.services.HostService;
import com.github.sacull.koturno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/groups")
public class GroupsPageController {

    private final HGroupService hGroupService;
    private final HostService hostService;
    private final UserService userService;

    @Autowired
    public GroupsPageController(HGroupService hGroupService,
                                HostService hostService,
                                UserService userService) {

        this.hGroupService = hGroupService;
        this.hostService = hostService;
        this.userService = userService;
    }

    @GetMapping
    public String serveGroupsPage(Model model, Principal principal) {

        model.addAttribute("firstUser", userService.countUsers() == 0);

        if (principal != null) {
            model.addAttribute("loggedUser", principal.getName());
        } else {
            model.addAttribute("loggedUser", null);
        }

        List<HGroup> hGroups = hGroupService.getAllGroups();
        model.addAttribute("groups", hGroups);

        List<Host> allHosts = hostService.getAllHosts();

        Map<String, Integer> hGroupMembersCounter = new HashMap<>();

        for (HGroup hGroup : hGroups) {
            hGroupMembersCounter.put(hGroup.getName(), 0);
        }

        for (Host host : allHosts) {
            hGroupMembersCounter.replace(host.getHostGroup().getName(),
                    hGroupMembersCounter.get(host.getHostGroup().getName()) + 1);
        }
        model.addAttribute("groupMembersCounter", hGroupMembersCounter);

        return "/WEB-INF/views/groups.jsp";
    }

    @PostMapping
    public String addNewHost(RedirectAttributes redirectAttributes,
                             Principal principal,
                             String name,
                             String description) {

        if (!userService.findByName(principal.getName()).getRole().equalsIgnoreCase("role_admin")
                && !userService.findByName(principal.getName()).getRole().equalsIgnoreCase("role_editor")) {
            return "redirect:/";
        }

        if (description == null) {
            description = "";
        }

        if (hGroupService.getGroupByName(name) == null) {
            HGroup hGroupToAdd = new HGroup(name, description);
            hGroupService.save(hGroupToAdd);
            redirectAttributes.addFlashAttribute("error", "group-created");
        } else {
            redirectAttributes.addFlashAttribute("error", "group-exists");
        }

        return "redirect:/groups";
    }
}
