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

import java.util.List;

@Controller
@RequestMapping("/host")
public class HostController {

    private final InaccessibilityRepository inaccessibilityRepository;
    private final HostRepository hostRepository;
    private final HGroupRepository hGroupRepository;

    @Autowired
    public HostController(InaccessibilityRepository inaccessibilityRepository,
                          HostRepository hostRepository,
                          HGroupRepository hGroupRepository) {
        this.inaccessibilityRepository = inaccessibilityRepository;
        this.hostRepository = hostRepository;
        this.hGroupRepository = hGroupRepository;
    }

    @GetMapping
    public String doSomethingWithHost(Model model,
                                      Long id,
                                      String action) {
        Host host = hostRepository.getOne(id);
        List<Inaccessibility> hostInaccessibilityList = inaccessibilityRepository.findAllByHostOrderByStartDesc(host);
        List<HGroup> hostGroupList = hGroupRepository.findAll();
        model.addAttribute("host", host);
        model.addAttribute("inaccessibilityList", hostInaccessibilityList);
        model.addAttribute("hostGroupList", hostGroupList);
        return "/WEB-INF/views/host.jsp";
    }

    @PostMapping
    public String editHost(RedirectAttributes redirectAttributes,
                             String originAddress,
                             String address,
                             String activity,
                             String name,
                             String description,
                             String hostGroupName) {
        Host hostToSave = hostRepository.findByAddress(originAddress);
        if (hostRepository.findByAddress(address) == null || address.equalsIgnoreCase(originAddress)) {
            HGroup hostGroup = hGroupRepository.findByName(hostGroupName);
            hostToSave.setAddress(address);
            if (activity.equalsIgnoreCase("Aktywny")) {
                hostToSave.setActive(true);
            } else {
                hostToSave.setActive(false);
            }
            hostToSave.setName(name);
            hostToSave.setDescription(description);
            hostToSave.setHostGroup(hostGroup);
            hostRepository.save(hostToSave);
            redirectAttributes.addFlashAttribute("error", "0");
        } else {
            redirectAttributes.addFlashAttribute("error", "1");
        }
        return "redirect:/host?id=" + hostToSave.getId() +"&action=info";
    }
}
