package com.github.sacull.koturno.controlers;

import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.utils.LifeChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Controller
public class HostsView {

    @Autowired
    EntityManager em;

    @Autowired
    LifeChecker lifeChecker;

    @GetMapping("/")
    public String showDashboard(Model model) {
        TypedQuery<Host> hostQuery = em.createQuery("SELECT h FROM Host h", Host.class);
        List<Host> hosts = hostQuery.getResultList();
        model.addAttribute("hosts", hosts);
        TypedQuery<Inaccessibility> inaccessibilityQuery =
                em.createQuery("SELECT i FROM Inaccessibility i", Inaccessibility.class);
        List<Inaccessibility> inaccessibilities = inaccessibilityQuery.getResultList();
        model.addAttribute("inaccessibilities", inaccessibilities);
        return "index";
    }
}
