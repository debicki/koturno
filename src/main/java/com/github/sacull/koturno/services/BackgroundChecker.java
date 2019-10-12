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
import java.util.ArrayList;
import java.util.List;
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
        List<Long> offlineHosts = new ArrayList<>();
        List<Long> instabilityHosts = new ArrayList<>();
        while (true) {
            logger.info("New scan started {}", LocalTime.now());
            for (Host host : hosts) {
                boolean isReachable = lifeChecker.isReachable(host);
                if (host.isActive() && isReachable && instabilityHosts.contains(host.getId())) {
                    offlineHosts.remove(host.getId());
                    instabilityHosts.remove(host.getId());
                    this.updateEndTime(host);
                    if (host.getHostname().equals("")) {
                        logger.info("Host {} is removed from offline/instability hosts list", host.getIPv4());
                    } else {
                        logger.info("Host {} is removed from offline/instability hosts list", host.getHostname());
                    }
                } else if (host.isActive() && !isReachable && !instabilityHosts.contains(host.getId())) {
                    instabilityHosts.add(host.getId());
                    this.setStartTime(host);
                    if (host.getHostname().equals("")) {
                        logger.info("Host {} is added to instability hosts list", host.getIPv4());
                    } else {
                        logger.info("Host {} is added to instability hosts list", host.getHostname());
                    }
                } else if (host.isActive() && !isReachable &&
                        instabilityHosts.contains(host.getId()) && !offlineHosts.contains(host.getId())) {
                    offlineHosts.add(host.getId());
                    this.setOfflineStatus(host);
                    if (host.getHostname().equals("")) {
                        logger.info("Host {} is added to offline hosts list", host.getIPv4());
                    } else {
                        logger.info("Host {} is added to offline hosts list", host.getHostname());
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

    private void setOfflineStatus(Host host) {
        Inaccessibility inaccessibilityToUpdate = this.getLastInaccessibility(host);
        inaccessibilityToUpdate.setOfflineStatus(true);
        inaccessibilityRepository.save(inaccessibilityToUpdate);
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
