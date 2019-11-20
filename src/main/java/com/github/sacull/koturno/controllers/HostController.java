package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.services.HGroupService;
import com.github.sacull.koturno.services.HostService;
import com.github.sacull.koturno.services.InaccessibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/host")
public class HostController {

    private InaccessibilityService inaccessibilityService;
    private HostService hostService;
    private HGroupService hGroupService;

    @Autowired
    public HostController(InaccessibilityService inaccessibilityService, HostService hostService, HGroupService hGroupService) {
        this.inaccessibilityService = inaccessibilityService;
        this.hostService = hostService;
        this.hGroupService = hGroupService;
    }

    @GetMapping
    public String doSomethingWithHost(RedirectAttributes redirectAttributes,
                                      Model model,
                                      Long id,
                                      @RequestParam(required = false, defaultValue = "info") String action) {
        Host host = hostService.getHostById(id);
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
            List<HGroup> hostGroupList = hGroupService.getAllGroups();
            model.addAttribute("host", host);
            model.addAttribute("inaccessibilityList", hostInaccessibilityList);
            model.addAttribute("hostGroupList", hostGroupList);
            return "/WEB-INF/views/host.jsp";
        }
    }

    @PostMapping
    public String editHost(RedirectAttributes redirectAttributes,
                             String originAddress,
                             String address,
                             String activity,
                             String name,
                             String description,
                             String hostGroupName) {
        Host hostToSave = hostService.getHostByAddress(originAddress);
        if (hostService.getHostByAddress(address) == null || address.equalsIgnoreCase(originAddress)) {
            HGroup hostGroup = hGroupService.getGroupByName(hostGroupName);
            hostToSave.setAddress(address);
            if (activity.equalsIgnoreCase("Aktywny")) {
                hostToSave.setActive(true);
            } else {
                hostToSave.setActive(false);
            }
            hostToSave.setName(name);
            hostToSave.setDescription(description);
            hostToSave.setHostGroup(hostGroup);
            hostService.save(hostToSave);
            redirectAttributes.addFlashAttribute("error", "0");
        } else {
            redirectAttributes.addFlashAttribute("error", "1");
        }
        return "redirect:/host?id=" + hostToSave.getId() +"&action=info";
    }
}
