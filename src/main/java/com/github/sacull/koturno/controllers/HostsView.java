package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.HGroupRepository;
import com.github.sacull.koturno.repositories.HostRepository;
import com.github.sacull.koturno.repositories.InaccessibilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class HostsView {

    @Autowired
    private EntityManager em;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private InaccessibilityRepository inaccessibilityRepository;

    @Autowired
    private HGroupRepository hGroupRepository;

    private Logger logger = LoggerFactory.getLogger("HostsView");

//    @GetMapping("/host/{id}")
//    public String showHost(Model model, @PathVariable String id) {
//        Host host = hostRepository.getById(Long.valueOf(id));
//        model.addAttribute("host", host);
//        List<Inaccessibility> hostInaccessibilities = host.getInaccessibilities();
//        Collections.reverse(hostInaccessibilities);
//        model.addAttribute("hostInaccessibilities", hostInaccessibilities);
//        return "host";
//    }
//
//    @GetMapping("/inaccessibility/{id}")
//    public String showInaccessibility(Model model, @PathVariable String id) {
//        Inaccessibility inaccessibility = inaccessibilityRepository.getById(Long.valueOf(id));
//        model.addAttribute("inaccessibility", inaccessibility);
//        Host host = hostRepository.getById(inaccessibility.getHost().getId());
//        model.addAttribute("host", host);
//        return "inaccessibility";
//    }
//
//    @GetMapping("/inaccessibility/edit/{id}")
//    public String editInaccessibility(Model model, @PathVariable String id) {
//        Inaccessibility inaccessibility = inaccessibilityRepository.getById(Long.valueOf(id));
//        model.addAttribute("inaccessibility", inaccessibility);
//        return "iedit";
//    }
//
//    @PostMapping("/inaccessibility/update/{id}")
//    public String updateInaccessibility(Model model,
//                                        @PathVariable String id,
//                                        @Valid Inaccessibility inaccessibility) {
//        Inaccessibility inaccessibilityToUpdate = inaccessibilityRepository.getById(Long.parseLong(id));
//        inaccessibilityToUpdate.setDescription(inaccessibility.getDescription());
//        inaccessibilityRepository.save(inaccessibilityToUpdate);
//        logger.info("Inaccessability {} was updated", inaccessibilityToUpdate.getId());
//        return showDashboard(model);
//    }
//
//    @GetMapping("/inaccessibility/ignore/{id}")
//    public String ignoreInaccessibility(Model model, @PathVariable String id) {
//        Inaccessibility inaccessibilityToIgnore = inaccessibilityRepository.getById(Long.parseLong(id));
//        inaccessibilityToIgnore.setActive(false);
//        inaccessibilityRepository.save(inaccessibilityToIgnore);
//        logger.info("Inaccessability {} was ignored", inaccessibilityToIgnore.getId());
//        return showDashboard(model);
//    }
//
//    @GetMapping("/inaccessibility/delete/{id}")
//    public String deleteInaccessibility(Model model, @PathVariable String id) {
//        Inaccessibility inaccessibilityToDelete = inaccessibilityRepository.getById(Long.parseLong(id));
//        inaccessibilityRepository.deleteById(Long.parseLong(id));
//        logger.info("Inaccessability {} was deleted", inaccessibilityToDelete.getId());
//        return showHistory(model, "1");
//    }
//
//    @GetMapping("/host/new")
//    public String newHost(Model model) {
//        Host host = new Host("",
//                "To pole nie może być puste!",
//                "",
//                null,
//                null);
//        model.addAttribute(host);
//        return "hnew";
//    }
//
//    @PostMapping("host/add")
//    public String addHost(Model model, @Valid Host host) {
//        if (!this.hostExists(host)) {
//            host.setHostGroup(hGroupRepository.getDefaultHostGroup());
//            host.clearInaccessibilities();
//            hostRepository.save(host);
//            logger.info("Host {} was added", host.getAddress());
//            return "asummary";
//        } else {
//            logger.info("Host {} wasn't added", host.getAddress());
//            return "aesummary";
//        }
//    }
//
//    @GetMapping("/host/edit/{id}")
//    public String editHost(Model model, @PathVariable String id) {
//        Host host = hostRepository.getById(Long.valueOf(id));
//        model.addAttribute("host", host);
//        return "hedit";
//    }
//
//    @PostMapping("/host/update/{id}")
//    public String updateHost(Model model,
//                             @PathVariable String id,
//                             @Valid Host host) {
//        Host hostToUpdate = hostRepository.getById(Long.parseLong(id));
//        hostToUpdate.setName(host.getName());
//        hostToUpdate.setDescription(host.getDescription());
//        hostRepository.save(hostToUpdate);
//        logger.info("Host {} was updated", hostToUpdate.getAddress());
//        return showDashboard(model);
//    }
//
//    @GetMapping("host/activate/{id}")
//    public String activateHost(Model model, @PathVariable String id) {
//        Host hostToActivate = hostRepository.getById(Long.parseLong(id));
//        hostToActivate.setActive(true);
//        hostRepository.save(hostToActivate);
//        logger.info("Host {} was activated", hostToActivate.getAddress());
//        return showHosts(model);
//    }
//
//    @GetMapping("host/deactivate/{id}")
//    public String deactivateHost(Model model, @PathVariable String id) {
//        Host hostToDeactivate = hostRepository.getById(Long.parseLong(id));
//        hostToDeactivate.setActive(false);
//        Inaccessibility inaccessibilityToDeactivate = inaccessibilityRepository.getLastHostInaccessibility(hostToDeactivate);
//        if (inaccessibilityToDeactivate != null) {
//            inaccessibilityToDeactivate.setActive(false);
//            inaccessibilityRepository.save(inaccessibilityToDeactivate);
//            hostRepository.save(hostToDeactivate);
//        }
//        logger.info("Host {} was deactivated", hostToDeactivate.getAddress());
//        return showHosts(model);
//    }
//
//    @GetMapping("/group/{id}")
//    public String showGroup(Model model, @PathVariable String id) {
//        HGroup group = hGroupRepository.getById(Long.parseLong(id));
//        model.addAttribute("group", group);
//        return "group";
//    }
//
//    @GetMapping("/group/add/{id}")
//    public String addHostToGroup(Model model, @PathVariable String id) {
//        HGroup group = hGroupRepository.getById(Long.parseLong(id));
//        model.addAttribute("group", group);
//        List<Host> hosts = hostRepository.getAllHosts();
//        model.addAttribute("hosts", hosts);
//        return "gadd";
//    }
//
//    @PostMapping("/group/update/{id}")
//    public String updateHostsInGroup(Model model, @PathVariable String id) {
//// TODO: 23.10.2019 Od tego miejsca należy rozpocząć pisanie. ;)
//        return "gupdate";
//    }
//
//    @GetMapping("/group/new")
//    public String addGroup(Model model) {
//        HGroup group = new HGroup("", "");
//        model.addAttribute("group", group);
//        return "gnew";
//    }
//
//    @PostMapping("/groups/update")
//    public String updateGroupsList(Model model, @Valid HGroup group) {
//        if (!this.groupExists(group)) {
//            hGroupRepository.save(group);
//            logger.info("Group {} was added", group.getName());
//            return "gnasummary";
//        } else {
//            logger.info("Group {} wasn't added", group.getName());
//            return "gnaesummary";
//        }
//    }
//
//    private boolean hostExists(Host host) {
//        List<Host> hostsInDatabase = hostRepository.getAllHosts();
//        for (Host hostFromDatabase : hostsInDatabase) {
//            if (hostFromDatabase.getAddress().equals(host.getAddress())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean groupExists(HGroup group) {
//        List<HGroup> groupsInDatabase = hGroupRepository.getAllGroups();
//        for (HGroup groupInDatabase : groupsInDatabase) {
//            if (group.getName().equals(groupInDatabase.getName())) {
//                return true;
//            }
//        }
//        return false;
//    }
}
