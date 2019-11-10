package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.HostRepository;
import com.github.sacull.koturno.repositories.InaccessibilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/hosts")
public class HostsPageController {

    private final HostRepository hostRepository;
    private final InaccessibilityRepository inaccessibilityRepository;

    @Autowired
    public HostsPageController(HostRepository hostRepository, InaccessibilityRepository inaccessibilityRepository) {
        this.hostRepository = hostRepository;
        this.inaccessibilityRepository = inaccessibilityRepository;
    }

    @GetMapping
    public String serveHostsPage(Model model) {
        List<Host> allHosts = hostRepository.findAllByOrderByName();
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
        model.addAttribute("unstableHosts", allUnstableHosts);
        model.addAttribute("offlineHosts", allOfflineHosts);
        model.addAttribute("disabledMenuItem", "hosts");
        return "/WEB-INF/views/hosts.jsp";
    }
}
