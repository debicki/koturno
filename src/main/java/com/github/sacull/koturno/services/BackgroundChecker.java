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
        List<Long> hostsWhichWasOffline = new ArrayList<>();
        while (true) {
            for (Host host : hosts) {
                boolean isReachable = lifeChecker.isReachable(host);
                if (isReachable && hostsWhichWasOffline.contains(host.getId())) {
                    hostsWhichWasOffline.remove(host.getId());
                    this.updateEndTime(host);
                    logger.info("Host {} is removed from offline hosts list", host.getDestination());
                } else if (!isReachable && hostsWhichWasOffline.contains(host.getId())) {
                    this.updateEndTime(host);
                    logger.info("Host {} has updated offline status", host.getDestination());
                } else if (!isReachable && !hostsWhichWasOffline.contains(host.getId())) {
                    hostsWhichWasOffline.add(host.getId());
                    this.setStartTime(host);
                    logger.info("Host {} is added to offline hosts list", host.getDestination());
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
        Inaccessibility inaccessibilityToOpen = new Inaccessibility(host, LocalDateTime.now(), LocalDateTime.now(), "");
        host.addInaccessibility(inaccessibilityToOpen);
        inaccessibilityRepository.save(inaccessibilityToOpen);
        hostRepository.save(host);
    }

    private void updateEndTime(Host host) {
        Inaccessibility inaccessibilityToUpdate = inaccessibilityRepository.getById(
                host.getInaccessibilities()
                        .get(host.getInaccessibilities().size() - 1)
                        .getId());
        inaccessibilityToUpdate.setEnd(LocalDateTime.now());
        inaccessibilityRepository.save(inaccessibilityToUpdate);
    }
}
