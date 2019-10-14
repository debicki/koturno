package com.github.sacull.koturno.controlers;

import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.HostRepository;
import com.github.sacull.koturno.repositories.InaccessibilityRepository;
import com.github.sacull.koturno.utils.LifeChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HostsView {

    @Autowired
    EntityManager em;

    @Autowired
    HostRepository hostRepository;

    @Autowired
    InaccessibilityRepository inaccessibilityRepository;

    @Autowired
    LifeChecker lifeChecker;

    @GetMapping("/")
    public String showDashboard(Model model) {
        List<Host> hosts = this.getAllHosts();
        List<Host> offlineHosts = new ArrayList<>();
        List<Host> instabilityHosts = new ArrayList<>();
        for (Host host : hosts) {
            if (host.getInaccessibilities().size() > 0 &&
                    host.getInaccessibilities().get(host.getInaccessibilities().size() - 1).isActive()) {
                if (host.getInaccessibilities().get(host.getInaccessibilities().size() - 1).isOfflineStatus()) {
                    offlineHosts.add(host);
                } else {
                    instabilityHosts.add(host);
                }
            }
        }
        model.addAttribute("offlineHosts", offlineHosts);
        model.addAttribute("instabilityHosts", instabilityHosts);
        return "dashboard";
    }

    @GetMapping("/host/{id}")
    public String showHost(Model model, @PathVariable String id) {
        Host host = hostRepository.getById(Long.valueOf(id));
        model.addAttribute("host", host);
        List<Inaccessibility> hostInaccessibilities = host.getInaccessibilities();
        model.addAttribute("hostInaccessibilities", hostInaccessibilities);
        return "host";
    }

    @GetMapping("/inaccessibility/{id}")
    public String showInaccessibility(Model model, @PathVariable String id) {
        Inaccessibility inaccessibility = inaccessibilityRepository.getById(Long.valueOf(id));
        model.addAttribute("inaccessibility", inaccessibility);
        Host host = hostRepository.getById(inaccessibility.getHost().getId());
        model.addAttribute("host", host);
        return "inaccessibility";
    }

    @GetMapping("/inaccessibility/edit/{id}")
    public String editInaccessibility(Model model, @PathVariable String id) {
        Inaccessibility inaccessibility = inaccessibilityRepository.getById(Long.valueOf(id));
        model.addAttribute("inaccessibility", inaccessibility);
        return "iedit";
    }

    @PostMapping("/inaccessibility/update/{id}")
    public String updateInaccessibility(Model model,
                                        @PathVariable String id,
                                        @Valid Inaccessibility inaccessibility) {
        Inaccessibility inaccessibilityToUpdate = inaccessibilityRepository.getById(Long.parseLong(id));
        inaccessibilityToUpdate.setDescription(inaccessibility.getDescription());
        inaccessibilityRepository.save(inaccessibilityToUpdate);
        return showDashboard(model);
    }

    @GetMapping("/inaccessibility/ignore/{id}")
    public String ignoreInaccessibility(Model model, @PathVariable String id) {
        Inaccessibility inaccessibilityToIgnore = inaccessibilityRepository.getById(Long.parseLong(id));
        inaccessibilityToIgnore.setActive(false);
        inaccessibilityRepository.save(inaccessibilityToIgnore);
        return showDashboard(model);
    }

    @GetMapping("/host/edit/{id}")
    public String editHost(Model model, @PathVariable String id) {
        Host host = hostRepository.getById(Long.valueOf(id));
        model.addAttribute("host", host);
        return "hedit";
    }

    @PostMapping("/host/update/{id}")
    public String updateHost(Model model,
                             @PathVariable String id,
                             @Valid Host host) {
        Host hostToUpdate = hostRepository.getById(Long.parseLong(id));
        hostToUpdate.setHostname(host.getHostname());
        hostToUpdate.setDescription(host.getDescription());
        hostRepository.save(hostToUpdate);
        return showDashboard(model);
    }

    @GetMapping("host/activate/{id}")
    public String activateHost(Model model, @PathVariable String id) {
        Host hostToActivate = hostRepository.getById(Long.parseLong(id));
        hostToActivate.setActive(true);
        hostRepository.save(hostToActivate);
        return showHosts(model);
    }

    @GetMapping("host/deactivate/{id}")
    public String deactivateHost(Model model, @PathVariable String id) {
        Host hostToDeactivate = hostRepository.getById(Long.parseLong(id));
        hostToDeactivate.setActive(false);
        Inaccessibility inaccessibilityToDeactivate = this.getLastHostInaccessibility(hostToDeactivate);
        inaccessibilityToDeactivate.setActive(false);
        inaccessibilityRepository.save(inaccessibilityToDeactivate);
        hostRepository.save(hostToDeactivate);
        return showHosts(model);
    }

    @GetMapping("/hosts")
    public String showHosts(Model model) {
        List<Host> hosts = this.getAllHosts();
        model.addAttribute("hosts", hosts);
        return "hosts";
    }

    @GetMapping("/history")
    public String showHistory(Model model) {
        List<Inaccessibility> inaccessibilities = this.getAllInaccessibilities();
        Integer numberOfinaccessibilities = inaccessibilities.size();
        List<Inaccessibility> activeInaccessibilities = new ArrayList<>();
        List<Inaccessibility> inactiveInaccessibilities = new ArrayList<>();
        for (int i = inaccessibilities.size() - 1; i >= 0; i--) {
            if (inaccessibilities.get(i).isActive()) {
                activeInaccessibilities.add(inaccessibilities.get(i));
            } else {
                inactiveInaccessibilities.add(inaccessibilities.get(i));
            }
        }
        model.addAttribute("numberOfinaccessibilities", numberOfinaccessibilities);
        model.addAttribute("activeInaccessibilities", activeInaccessibilities);
        model.addAttribute("inactiveInaccessibilities", inactiveInaccessibilities);
        return "history";
    }

    private List<Host> getAllHosts() {
        TypedQuery<Host> hostQuery = em.createQuery("SELECT h FROM Host h", Host.class);
        return hostQuery.getResultList();
    }

    private List<Inaccessibility> getAllInaccessibilities() {
        TypedQuery<Inaccessibility> inaccessibilityQuery =
                em.createQuery("SELECT i FROM Inaccessibility i", Inaccessibility.class);
        return inaccessibilityQuery.getResultList();
    }

    private Inaccessibility getLastHostInaccessibility(Host host) {
        return inaccessibilityRepository.getById(
                host.getInaccessibilities()
                        .get(host.getInaccessibilities().size() - 1)
                        .getId());
    }
}
