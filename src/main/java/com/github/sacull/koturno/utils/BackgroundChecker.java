package com.github.sacull.koturno.utils;

import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.services.HostService;
import com.github.sacull.koturno.services.IGroupService;
import com.github.sacull.koturno.services.InaccessibilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BackgroundChecker {

    private HostService hostService;
    private InaccessibilityService inaccessibilityService;
    private IGroupService iGroupService;
    private LifeChecker lifeChecker;

    private List<Long> offlineHosts = new ArrayList<>();
    private List<Long> instabilityHosts = new ArrayList<>();

    @Autowired
    public BackgroundChecker(HostService hostService,
                             InaccessibilityService inaccessibilityService,
                             IGroupService iGroupService,
                             LifeChecker lifeChecker) {
        this.hostService = hostService;
        this.inaccessibilityService = inaccessibilityService;
        this.iGroupService = iGroupService;
        this.lifeChecker = lifeChecker;
    }

    @Scheduled(fixedDelay = 5000, initialDelay = 5000)
    public void runChecker() {
        List<Host> hosts;
        hosts = hostService.getAllHosts();
        log.info("New scan started {}", LocalTime.now());
        for (Host host : hosts) {
            boolean isReachable = lifeChecker.isReachable(host);
            if (host.isActive() && isReachable && instabilityHosts.contains(host.getId())) {
                offlineHosts.remove(host.getId());
                instabilityHosts.remove(host.getId());
                this.updateEndTime(host);
                if (host.getName().equals("")) {
                    log.info("Host {} is removed from offline/instability hosts list", host.getAddress());
                } else {
                    log.info("Host {} is removed from offline/instability hosts list", host.getName());
                }
            } else if (host.isActive() && !isReachable && !instabilityHosts.contains(host.getId())) {
                instabilityHosts.add(host.getId());
                this.setStartTime(host);
                if (host.getName().equals("")) {
                    log.info("Host {} is added to instability hosts list", host.getAddress());
                } else {
                    log.info("Host {} is added to instability hosts list", host.getName());
                }
            } else if (host.isActive() && !isReachable &&
                    instabilityHosts.contains(host.getId()) && !offlineHosts.contains(host.getId())) {
                offlineHosts.add(host.getId());
                this.setOfflineStatus(host);
                if (host.getName().equals("")) {
                    log.info("Host {} is added to offline hosts list", host.getAddress());
                } else {
                    log.info("Host {} is added to offline hosts list", host.getName());
                }
            }
        }
    }

    private void setStartTime(Host host) {
        Inaccessibility inaccessibilityToOpen =
                new Inaccessibility(host, "", iGroupService.getGroup("default"));
        inaccessibilityService.save(inaccessibilityToOpen);
    }

    private void setOfflineStatus(Host host) {
        Inaccessibility inaccessibilityToUpdate = inaccessibilityService.getLastInaccessibilityByHost(host);
        inaccessibilityToUpdate.setOfflineStatus(true);
        inaccessibilityService.save(inaccessibilityToUpdate);
    }

    private void updateEndTime(Host host) {
        Inaccessibility inaccessibilityToUpdate = inaccessibilityService.getLastInaccessibilityByHost(host);
        inaccessibilityToUpdate.setEnd(LocalDateTime.now());
        inaccessibilityToUpdate.setActive(false);
        inaccessibilityService.save(inaccessibilityToUpdate);
    }
}
