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
import java.util.List;

@Controller
@RequestMapping("/host")
public class HostPageController {

    private final InaccessibilityService inaccessibilityService;
    private final HostService hostService;
    private final HGroupService hGroupService;
    private final UserService userService;

    @Autowired
    public HostPageController(InaccessibilityService inaccessibilityService,
                              HostService hostService,
                              HGroupService hGroupService,
                              UserService userService) {

        this.inaccessibilityService = inaccessibilityService;
        this.hostService = hostService;
        this.hGroupService = hGroupService;
        this.userService = userService;
    }

    @GetMapping
    public String doSomethingWithHost(RedirectAttributes redirectAttributes,
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

        Host host = hostService.getHostById(id);
        model.addAttribute("host", host);

        if (action.equalsIgnoreCase("remove")) {
            List<Inaccessibility> hostInaccessibilityList = inaccessibilityService.findAllByHost(host);

            for (Inaccessibility inaccessibility : hostInaccessibilityList) {
                inaccessibilityService.delete(inaccessibility);
            }
            hostService.delete(host);

            redirectAttributes.addFlashAttribute("error", "10");
            return "redirect:/hosts";
        } else {
            List<Inaccessibility> hostInaccessibilityList = inaccessibilityService.findAllByHostOrderByStartDesc(host);
            model.addAttribute("inaccessibilityList", hostInaccessibilityList);

            List<HGroup> hostGroupList = hGroupService.getAllGroups();
            model.addAttribute("hostGroupList", hostGroupList);

            return "/WEB-INF/views/host.jsp";
        }
    }

    @PostMapping
    public String editHost(RedirectAttributes redirectAttributes,
                           Principal principal,
                           String originAddress,
                           String address,
                           String activity,
                           String name,
                           String description,
                           String hostGroupName) {

        if (!userService.findByName(principal.getName()).getRole().equalsIgnoreCase("role_admin")
                && !userService.findByName(principal.getName()).getRole().equalsIgnoreCase("role_editor")) {
            return "redirect:/";
        }

        Host hostToSave = hostService.getHostByAddress(originAddress);

        if (hostService.getHostByAddress(address) == null || address.equalsIgnoreCase(originAddress)) {
            HGroup hostGroup = hGroupService.getGroupByName(hostGroupName);
            hostToSave.setAddress(address);

            hostToSave.setActive(activity.equalsIgnoreCase("Aktywny"));

            hostToSave.setName(name);
            hostToSave.setDescription(description);
            hostToSave.setHostGroup(hostGroup);
            hostService.save(hostToSave);

            redirectAttributes.addFlashAttribute("error", "0");
        } else {
            redirectAttributes.addFlashAttribute("error", "1");
        }

        return "redirect:/host?id=" + hostToSave.getId() + "&action=info";
    }
}
