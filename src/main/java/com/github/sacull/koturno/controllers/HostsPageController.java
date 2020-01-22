package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.entities.User;
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

    private HostService hostService;
    private InaccessibilityService inaccessibilityService;
    private HGroupService hGroupService;
    private UserService userService;
    private FileService fileService;

    @Autowired
    public HostsPageController(HostService hostService,
                               InaccessibilityService inaccessibilityService,
                               HGroupService hGroupService,
                               UserService userService,
                               FileService fileService) {
        this.hostService = hostService;
        this.inaccessibilityService = inaccessibilityService;
        this.hGroupService = hGroupService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping
    public String serveHostsPage(Model model,
                                 Principal principal) {
        User loggedUser = userService.findByName(principal.getName());
        List<Host> allHosts = hostService.findAllByByOwnerOrderByName(loggedUser);
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
                             String hostGroupName,
                             Principal principal) {
        User user = userService.findByName(principal.getName());
        if (name == null) {
            name = "";
        }
        if (description == null) {
            description = "";
        }
        if (hostService.getHostByAddress(address, user) == null) {
            HGroup hostGroup = hGroupService.getGroupByName(hostGroupName);
            Host hostToAdd = new Host(name, address, description, hostGroup, user);
            if (activity.equalsIgnoreCase("Nieaktywny")) {
                hostToAdd.setActive(false);
            }
            hostService.save(hostToAdd);
            if (fileService.isValidAddress(address)) {
                redirectAttributes.addFlashAttribute("error", "0");
            } else {
                redirectAttributes.addFlashAttribute("error", "3");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "1");
        }
        return "redirect:/hosts";
    }

    @PostMapping("/import")
    public String importHosts(RedirectAttributes redirectAttributes,
                              MultipartFile file,
                              Principal principal) throws IOException, CsvException {

        Map<String, Integer> emptyReport = new HashMap<>();
        emptyReport.put("importSuccess", 0);
        emptyReport.put("importWarnings", 0);
        emptyReport.put("importErrors", 0);

        User loggedUser = userService.findByName(principal.getName());
        Map<String, Integer> report = fileService.hostsImport(loggedUser, emptyReport, file);

        redirectAttributes.addFlashAttribute("importSuccess", report.get("importSuccess"));
        redirectAttributes.addFlashAttribute("importWarnings", report.get("importWarnings"));
        redirectAttributes.addFlashAttribute("importErrors", report.get("importErrors"));
        return "redirect:/hosts";
    }
}
