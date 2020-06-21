package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.services.*;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hosts")
public class HostsPageController {

    private final HostService hostService;
    private final InaccessibilityService inaccessibilityService;
    private final HGroupService hGroupService;
    private final FileService fileService;
    private final UserService userService;

    @Autowired
    public HostsPageController(HostService hostService,
                               InaccessibilityService inaccessibilityService,
                               HGroupService hGroupService,
                               FileService fileService,
                               UserService userService) {

        this.hostService = hostService;
        this.inaccessibilityService = inaccessibilityService;
        this.hGroupService = hGroupService;
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping
    public String serveHostsPage(Model model, Principal principal) {

        model.addAttribute("firstUser", userService.countUsers() == 0);

        if (principal != null) {
            model.addAttribute("loggedUser", principal.getName());
        } else {
            model.addAttribute("loggedUser", null);
        }

        List<Host> allHosts = hostService.findAllByName();
        model.addAttribute("hosts", allHosts);

        List<HGroup> hostGroupList = hGroupService.getAllGroups();
        model.addAttribute("hostGroupList", hostGroupList);

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

        model.addAttribute("disabledMenuItem", "hosts");
        return "/WEB-INF/views/hosts.jsp";
    }

    @PostMapping
    public String addNewHost(RedirectAttributes redirectAttributes,
                             Principal principal,
                             String address,
                             String activity,
                             String name,
                             String description,
                             String externalLink,
                             String hostGroupName) {

        if (!userService.findByName(principal.getName()).getRole().equalsIgnoreCase("role_admin")
                && !userService.findByName(principal.getName()).getRole().equalsIgnoreCase("role_editor")) {
            return "redirect:/";
        }

        if (name == null) {
            name = "";
        }

        if (description == null) {
            description = "";
        }

        if (externalLink == null) {
            externalLink = "";
        }

        if (hostService.getHostByAddress(address) == null) {
            HGroup hostGroup = hGroupService.getGroupByName(hostGroupName);
            Host hostToAdd = new Host(name, address, description, externalLink, hostGroup);

            if (activity.equalsIgnoreCase("Nieaktywny")) {
                hostToAdd.setActive(false);
            }

            hostService.save(hostToAdd);

            if (fileService.isValidAddress(address)) {
                redirectAttributes.addFlashAttribute("error", "host-added");
            } else {
                redirectAttributes.addFlashAttribute("error", "check-host");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "host-exists");
        }

        return "redirect:/hosts";
    }

    @PostMapping("/import")
    public String importHosts(RedirectAttributes redirectAttributes,
                              Principal principal,
                              MultipartFile file) throws IOException, CsvException {

        if (!userService.findByName(principal.getName()).getRole().equalsIgnoreCase("role_admin")
                && !userService.findByName(principal.getName()).getRole().equalsIgnoreCase("role_editor")) {
            return "redirect:/";
        }

        Map<String, Integer> emptyReport = new HashMap<>();
        emptyReport.put("importSuccess", 0);
        emptyReport.put("importWarnings", 0);
        emptyReport.put("importErrors", 0);

        Map<String, Integer> report = fileService.hostsImport(emptyReport, file);

        redirectAttributes.addFlashAttribute("importSuccess", report.get("importSuccess"));
        redirectAttributes.addFlashAttribute("importWarnings", report.get("importWarnings"));
        redirectAttributes.addFlashAttribute("importErrors", report.get("importErrors"));
        return "redirect:/hosts";
    }
}
