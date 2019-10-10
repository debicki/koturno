package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.HostRepository;
import com.github.sacull.koturno.repositories.InaccessibilityRepository;
import com.github.sacull.koturno.utils.LifeChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class BackgroundChecker {

    @Autowired
    EntityManager em;

    @Autowired
    HostRepository hostRepository;

    @Autowired
    InaccessibilityRepository inaccessibilityRepository;

    @Autowired
    LifeChecker lifeChecker;

    private Logger logger = LoggerFactory.getLogger("BackgroundChecker");

    @Async
    public CompletableFuture<String> start() {
        TypedQuery<Host> query = em.createQuery("SELECT h FROM Host h", Host.class);
        List<Host> hosts = query.getResultList();
        Map<Long, Integer> problematicHosts = new HashMap<>();
        while (true) {
            logger.info("New scan started {}", LocalTime.now());
            for (Host host : hosts) {
                boolean isReachable = lifeChecker.isReachable(host);
                if (isReachable && problematicHosts.containsKey(host.getId())) {
                    if (problematicHosts.get(host.getId()) <= 1) {
                        problematicHosts.remove(host.getId());
                        this.updateEndTime(host);
                        if (host.getHostname().equals("")) {
                            logger.info("Host {} is removed from problematic hosts list", host.getIPv4());
                        } else {
                            logger.info("Host {} is removed from problematic hosts list", host.getHostname());
                        }
                    } else if (problematicHosts.get(host.getId()) == 2) {
                        problematicHosts.replace(host.getId(), 1);
                    } else {
                        problematicHosts.replace(host.getId(), 2);
                    }
                } else if (host.isActive() && !isReachable && !problematicHosts.containsKey(host.getId())) {
                    problematicHosts.put(host.getId(), 1);
                    this.setStartTime(host);
                    if (host.getHostname().equals("")) {
                        logger.info("Host {} is added to problematic hosts list", host.getIPv4());
                    } else {
                        logger.info("Host {} is added to problematic hosts list", host.getHostname());
                    }
                } else if (host.isActive() && !isReachable && problematicHosts.containsKey(host.getId())) {
                    if (problematicHosts.get(host.getId()) <= 1) {
                        problematicHosts.replace(host.getId(), 2);
                    } else {
                        problematicHosts.replace(host.getId(), 3);
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("BackgroundChecker sleep time interrupted");
            }
        }
    }

    private void setStartTime(Host host) {
        Inaccessibility inaccessibilityToOpen = new Inaccessibility(host, "");
        host.addInaccessibility(inaccessibilityToOpen);
        inaccessibilityRepository.save(inaccessibilityToOpen);
        hostRepository.save(host);
    }

    private void updateEndTime(Host host) {
        Inaccessibility inaccessibilityToUpdate = this.getLastInaccessibility(host);
        inaccessibilityToUpdate.setEnd(LocalDateTime.now());
        inaccessibilityToUpdate.setActive(false);
        inaccessibilityRepository.save(inaccessibilityToUpdate);
    }

    private Inaccessibility getLastInaccessibility(Host host) {
        return inaccessibilityRepository.getById(
                host.getInaccessibilities()
                        .get(host.getInaccessibilities().size() - 1)
                        .getId());
    }
}
