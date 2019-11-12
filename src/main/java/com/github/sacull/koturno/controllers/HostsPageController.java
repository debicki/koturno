package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.HGroupRepository;
import com.github.sacull.koturno.repositories.HostRepository;
import com.github.sacull.koturno.repositories.InaccessibilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/hosts")
public class HostsPageController {

    private final HostRepository hostRepository;
    private final InaccessibilityRepository inaccessibilityRepository;
    private final HGroupRepository hGroupRepository;

    @Autowired
    public HostsPageController(HostRepository hostRepository,
                               InaccessibilityRepository inaccessibilityRepository,
                               HGroupRepository hGroupRepository) {
        this.hostRepository = hostRepository;
        this.inaccessibilityRepository = inaccessibilityRepository;
        this.hGroupRepository = hGroupRepository;
    }

    @GetMapping
    public String serveHostsPage(Model model) {
        List<Host> allHosts = hostRepository.findAllByOrderByName();
        List<HGroup> hostGroupList = hGroupRepository.findAll();
        List<Inaccessibility> allInaccessibilityList = inaccessibilityRepository.findAllByActiveIsTrue();
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
        if (hostRepository.findByAddress(address) == null) {
            HGroup hostGroup = hGroupRepository.findByName(hostGroupName);
            Host hostToAdd = new Host(name, address, description, hostGroup);
            if (activity.equalsIgnoreCase("Nieaktywny")) {
                hostToAdd.setActive(false);
            }
            hostRepository.save(hostToAdd);
            redirectAttributes.addFlashAttribute("error", "0");
        } else {
            redirectAttributes.addFlashAttribute("error", "1");
        }
        return "redirect:/hosts";
    }
}
