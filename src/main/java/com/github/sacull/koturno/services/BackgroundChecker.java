package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.HostRepository;
import com.github.sacull.koturno.repositories.IGroupRepository;
import com.github.sacull.koturno.repositories.InaccessibilityRepository;
import com.github.sacull.koturno.utils.LifeChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class BackgroundChecker {

    @Autowired
    private EntityManager em;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private InaccessibilityRepository inaccessibilityRepository;

    @Autowired
    private IGroupRepository iGroupRepository;

    @Autowired
    private LifeChecker lifeChecker;

    private Logger logger = LoggerFactory.getLogger("BackgroundChecker");

    @Async
    public CompletableFuture<String> start() {
        List<Host> hosts;
        List<Long> offlineHosts = new ArrayList<>();
        List<Long> instabilityHosts = new ArrayList<>();
        while (true) {
            hosts = hostRepository.findAll();
            logger.info("New scan started {}", LocalTime.now());
            for (Host host : hosts) {
                boolean isReachable = lifeChecker.isReachable(host);
                if (host.isActive() && isReachable && instabilityHosts.contains(host.getId())) {
                    offlineHosts.remove(host.getId());
                    instabilityHosts.remove(host.getId());
                    this.updateEndTime(host);
                    if (host.getName().equals("")) {
                        logger.info("Host {} is removed from offline/instability hosts list", host.getAddress());
                    } else {
                        logger.info("Host {} is removed from offline/instability hosts list", host.getName());
                    }
                } else if (host.isActive() && !isReachable && !instabilityHosts.contains(host.getId())) {
                    instabilityHosts.add(host.getId());
                    this.setStartTime(host);
                    if (host.getName().equals("")) {
                        logger.info("Host {} is added to instability hosts list", host.getAddress());
                    } else {
                        logger.info("Host {} is added to instability hosts list", host.getName());
                    }
                } else if (host.isActive() && !isReachable &&
                        instabilityHosts.contains(host.getId()) && !offlineHosts.contains(host.getId())) {
                    offlineHosts.add(host.getId());
                    this.setOfflineStatus(host);
                    if (host.getName().equals("")) {
                        logger.info("Host {} is added to offline hosts list", host.getAddress());
                    } else {
                        logger.info("Host {} is added to offline hosts list", host.getName());
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
        Inaccessibility inaccessibilityToOpen =
                new Inaccessibility(host, "", iGroupRepository.findByName("default"));
        inaccessibilityRepository.save(inaccessibilityToOpen);
    }

    private void setOfflineStatus(Host host) {
        Inaccessibility inaccessibilityToUpdate = inaccessibilityRepository.findByHostOrderByEndDesc(host);
        inaccessibilityToUpdate.setOfflineStatus(true);
        inaccessibilityRepository.save(inaccessibilityToUpdate);
    }

    private void updateEndTime(Host host) {
        Inaccessibility inaccessibilityToUpdate = inaccessibilityRepository.findByHostOrderByEndDesc(host);
        inaccessibilityToUpdate.setEnd(LocalDateTime.now());
        inaccessibilityToUpdate.setActive(false);
        inaccessibilityRepository.save(inaccessibilityToUpdate);
    }
}
