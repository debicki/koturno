package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.services.HGroupService;
import com.github.sacull.koturno.services.HostService;
import com.github.sacull.koturno.services.InaccessibilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/hosts")
@Slf4j
public class HostsPageController {

    private HostService hostService;
    private InaccessibilityService inaccessibilityService;
    private HGroupService hGroupService;

    @Autowired
    public HostsPageController(HostService hostService, InaccessibilityService inaccessibilityService, HGroupService hGroupService) {
        this.hostService = hostService;
        this.inaccessibilityService = inaccessibilityService;
        this.hGroupService = hGroupService;
    }

    @GetMapping
    public String serveHostsPage(Model model) {
        List<Host> allHosts = hostService.findAllByOrderByName();
        List<HGroup> hostGroupList = hGroupService.getAllGroups();
        List<Inaccessibility> allInaccessibilityList = inaccessibilityService.findAllByActiveIsTrue();
        List<Host> allUnstableHosts = new ArrayList<>();
        List<Host> allOfflineHosts = new ArrayList<>();
        for (Inaccessibility inaccessibility : allInaccessibilityList) {
            if (inaccessibility.isOfflineStatus()) {
                allOfflineHosts.add(inaccessibility.getHost());
            } else {
                allUnstableHosts.add(inaccessibility.getHost());
            }
        }
        model.addAttribute("hosts", allHosts);
        model.addAttribute("hostGroupList", hostGroupList);
        model.addAttribute("unstableHosts", allUnstableHosts);
        model.addAttribute("offlineHosts", allOfflineHosts);
        model.addAttribute("disabledMenuItem", "hosts");
        return "/WEB-INF/views/hosts.jsp";
    }

    @PostMapping
    public String addNewHost(RedirectAttributes redirectAttributes,
                             String address,
                             String activity,
                             String name,
                             String description,
                             String hostGroupName) {
        if (name == null) {
            name = "";
        }
        if (description == null) {
            description = "";
        }
        if (hostService.getHostByAddress(address) == null) {
            HGroup hostGroup = hGroupService.getGroupByName(hostGroupName);
            Host hostToAdd = new Host(name, address, description, hostGroup);
            if (activity.equalsIgnoreCase("Nieaktywny")) {
                hostToAdd.setActive(false);
            }
            hostService.save(hostToAdd);
            if (isValidAddress(address)) {
                redirectAttributes.addFlashAttribute("error", "0");
            } else {
                redirectAttributes.addFlashAttribute("error", "3");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "1");
        }
        return "redirect:/hosts";
    }

    private boolean isValidAddress(String address) {
        InetAddress host;
        try {
            host = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            return false;
        }
        return true;
    }
}
