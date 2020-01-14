package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.entities.User;
import com.github.sacull.koturno.services.HGroupService;
import com.github.sacull.koturno.services.HostService;
import com.github.sacull.koturno.services.InaccessibilityService;
import com.github.sacull.koturno.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hosts")
@Slf4j
public class HostsPageController {

    private HostService hostService;
    private InaccessibilityService inaccessibilityService;
    private HGroupService hGroupService;
    private UserService userService;

    @Autowired
    public HostsPageController(HostService hostService, InaccessibilityService inaccessibilityService, HGroupService hGroupService, UserService userService) {
        this.hostService = hostService;
        this.inaccessibilityService = inaccessibilityService;
        this.hGroupService = hGroupService;
        this.userService = userService;
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
        if (hostService.getHostByAddress(address) == null) {
            HGroup hostGroup = hGroupService.getGroupByName(hostGroupName);
            Host hostToAdd = new Host(name, address, description, hostGroup, user);
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

    @PostMapping("/import")
    public String importHosts(RedirectAttributes redirectAttributes,
                              MultipartFile file,
                              Principal principal) throws IOException {
        int importSuccess = 0;
        int importWarnings = 0;
        int importErrors = 0;

        List<Host> importList = new ArrayList<>();
        BufferedReader fileContent = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        while ((line = fileContent.readLine()) != null) {
            if (!line.trim().startsWith("#") && !line.trim().startsWith("//") && !(line.trim().length() < 1)) {
                Host hostToAdd = parse(line);
                User user = userService.findByName(principal.getName());
                hostToAdd.setOwner(user);
                importList.add(hostToAdd);
            }
        }

        importErrors = importList.size();
        User loggedUser = userService.findByName(principal.getName());
        List<Host> hostsInDatabase = hostService.getAllHostsByUser(loggedUser);
        for (Host host : hostsInDatabase) {
            importList = importList.stream().filter(x -> !x.compareAddress(host)).collect(Collectors.toList());
        }
        importErrors -= importList.size();

        HGroup defaultGroup = hGroupService.getGroupByName("default");
        for (Host host : importList) {
            if (host.getName().equals("") || host.getName() == null) {
                host.setHostGroup(defaultGroup);
            } else {
                HGroup group = hGroupService.getGroupByName(host.getName());
                if (group == null) {
                    group = new HGroup(host.getName(), "");
                    group = hGroupService.save(group);
                }
                host.setHostGroup(group);
            }
            host.setOwner(loggedUser);
            if (isValidAddress(host.getAddress())) {
                importSuccess++;
            } else {
                importWarnings++;
            }
        }

        if (importList.size() > 0) {
            for (Host host : importList) {
                hostService.save(host);
            }
        }

        redirectAttributes.addFlashAttribute("importSuccess", importSuccess);
        redirectAttributes.addFlashAttribute("importWarnings", importWarnings);
        redirectAttributes.addFlashAttribute("importErrors", importErrors);
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

    private Host parse (String line) {
        int charCounter = 0;
        line = line.replace('\t', ' ');
        StringBuilder address = new StringBuilder();
        StringBuilder name = new StringBuilder();
        StringBuilder description = new StringBuilder();
        while (line.charAt(charCounter) == ' ') {
            charCounter++;
        }
        while ((charCounter < line.length()) && (line.charAt(charCounter) != ' ')) {
            address.append(line.charAt(charCounter));
            charCounter++;
        }
        while ((charCounter < line.length()) && (line.charAt(charCounter) == ' ')) {
            charCounter++;
        }
        String[] descriptionElements = line.substring(charCounter).split("\\*");
        if (descriptionElements.length > 1) {
            name.append(descriptionElements[0]);
            for (int i = 1; i < descriptionElements.length; i++) {
                description.append(descriptionElements[i]);
                description.append(' ');
            }
        } else if (descriptionElements.length == 1) {
            description.append(descriptionElements[0]);
        }
        return new Host(name.toString(), address.toString(), description.toString(), null, null);
    }
}
