package com.github.sacull.koturno.controlers;

import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.utils.LifeChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@RestController
public class HostsView {

    @Autowired
    EntityManager em;

    @Autowired
    LifeChecker lifeChecker;

    @RequestMapping("/")
    public String viewIndex() {
        StringBuilder result = new StringBuilder("<html><h1> >< Koturno ><</h1>");
        TypedQuery<Host> query = em.createQuery("SELECT h FROM Host h", Host.class);
        List<Host> hosts = query.getResultList();
        if (hosts.size() == 0) {
            result.append("No hosts in database!<br>");
        } else {
            for (Host host : hosts) {
                result.append("Destination: ");
                result.append(host.getDestination());
                if (lifeChecker.isReachable(host)) {
                    result.append(" is ONLINE<br>");
                } else {
                    result.append(" is OFFLINE<br>");
                }
            }
        }
        result.append("</html>");
        return result.toString();
    }
}
