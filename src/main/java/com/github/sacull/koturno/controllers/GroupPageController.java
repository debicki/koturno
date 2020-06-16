package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.services.HGroupService;
import com.github.sacull.koturno.services.HostService;
import com.github.sacull.koturno.services.InaccessibilityService;
import com.github.sacull.koturno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/group")
public class GroupPageController {

    private final HGroupService hGroupService;
    private final HostService hostService;
    private final InaccessibilityService inaccessibilityService;
    private final UserService userService;

    @Autowired
    public GroupPageController(HGroupService hGroupService,
                               HostService hostService,
                               InaccessibilityService inaccessibilityService,
                               UserService userService) {

        this.hGroupService = hGroupService;
        this.hostService = hostService;
        this.inaccessibilityService = inaccessibilityService;
        this.userService = userService;
    }

    @GetMapping
    public String doSomethingWithGroup(RedirectAttributes redirectAttributes,
                                       Model model,
                                       Principal principal,
                                       Long id,
                                       @RequestParam(required = false, defaultValue = "info") String action) {

        model.addAttribute("firstUser", userService.countUsers() == 0);

        if (principal != null) {
            model.addAttribute("loggedUser", principal.getName());
        } else {
            model.addAttribute("loggedUser", null);
        }

        HGroup hGroup = hGroupService.getGroupById(id);
        model.addAttribute("group", hGroup);

        List<Host> groupHosts = hostService.findAllByHostGroup(hGroup);
        model.addAttribute("hosts", groupHosts);

        if (action.equalsIgnoreCase("remove")) {
            if (hGroup.getName().equalsIgnoreCase("default")) {
                redirectAttributes.addFlashAttribute("error", "12");
                return "redirect:/groups";
            } else if (groupHosts.size() > 0 || hostService.countAllByHostGroup(hGroup) > 0) {
                redirectAttributes.addFlashAttribute("error", "11");
                return "redirect:/groups";
            } else {
                hGroupService.delete(hGroup);
                redirectAttributes.addFlashAttribute("error", "10");
                return "redirect:/groups";
            }
        } else {
            List<Inaccessibility> allInaccessibilityList = inaccessibilityService.findAllByActiveIsTrue();
            List<Host> allUnstableHosts = new ArrayList<>();
            List<Host> allOfflineHosts = new ArrayList<>();
            for (Inaccessibility inaccessibility : allInaccessibilityList) {
                if (inaccessibility.isOfflineStatus()) {
                    allOfflineHosts.add(inaccessibility.getHost());
                    model.addAttribute("offlineHosts", allOfflineHosts);
                } else {
                    allUnstableHosts.add(inaccessibility.getHost());
                    model.addAttribute("unstableHosts", allUnstableHosts);
                }
            }

            return "/WEB-INF/views/group.jsp";
        }
    }

    @PostMapping
    public String editGroup(RedirectAttributes redirectAttributes,
                            String originName,
                            String name,
                            String description) {

        HGroup groupToSave = hGroupService.getGroupByName(originName);

        if (originName.equalsIgnoreCase("default")) {
            redirectAttributes.addFlashAttribute("error", "3");
        } else if (hGroupService.getGroupByName(name) == null || name.equalsIgnoreCase(originName)) {
            groupToSave.setName(name);
            groupToSave.setDescription(description);
            hGroupService.save(groupToSave);
            redirectAttributes.addFlashAttribute("error", "0");
        } else {
            redirectAttributes.addFlashAttribute("error", "2");
        }

        return "redirect:/group?id=" + groupToSave.getId() + "&action=info";
    }
}
