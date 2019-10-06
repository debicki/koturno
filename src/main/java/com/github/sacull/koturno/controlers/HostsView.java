package com.github.sacull.koturno.controlers;

import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.HostRepository;
import com.github.sacull.koturno.utils.LifeChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HostsView {

    @Autowired
    EntityManager em;

    @Autowired
    HostRepository hostRepository;

    @Autowired
    LifeChecker lifeChecker;

    @GetMapping("/")
    public String showDashboard(Model model) {
        List<Host> hosts = this.getAllHosts();
        List<Host> offlineHosts = new ArrayList<>();
        for (Host host : hosts) {
            if (host.getInaccessibilities().size() > 0 &&
                    host.getInaccessibilities().get(host.getInaccessibilities().size() - 1).isActive()) {
                offlineHosts.add(host);
            }
        }
        model.addAttribute("offlineHosts", offlineHosts);
        return "dashboard";
    }

    @GetMapping("/host/{id}")
    public String showHost(Model model, @PathVariable String id) {
        Host host = hostRepository.getById(Long.valueOf(id));
        model.addAttribute("host", host);
        List<Inaccessibility> inaccessibilities = this.getAllInaccessibilities();
        List<Inaccessibility> hostInaccessibilities = new ArrayList<>();
        for (Inaccessibility inaccessibility : inaccessibilities) {
            if (inaccessibility.getHost().getId().equals(Long.valueOf(id))) {
                hostInaccessibilities.add(inaccessibility);
            }
        }
        model.addAttribute("hostInaccessibilities", hostInaccessibilities);
        return "host";
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
        List<Inaccessibility> revInaccessibilities = new ArrayList<>();
        for (int i = inaccessibilities.size() - 1; i >= 0; i--) {
            revInaccessibilities.add(inaccessibilities.get(i));
        }
        model.addAttribute("revInaccessibilities", revInaccessibilities);
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
}
