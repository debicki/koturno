package com.github.sacull.koturno;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.IGroup;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.services.HGroupService;
import com.github.sacull.koturno.services.IGroupService;
import com.github.sacull.koturno.services.InaccessibilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class KoturnoInitializer implements CommandLineRunner {

    private HGroupService hGroupService;
    private IGroupService iGroupService;
    private InaccessibilityService inaccessibilityService;

    @Autowired
    public KoturnoInitializer(HGroupService hGroupService,
                              IGroupService iGroupService,
                              InaccessibilityService inaccessibilityService) {
        this.hGroupService = hGroupService;
        this.iGroupService = iGroupService;
        this.inaccessibilityService = inaccessibilityService;
    }

    @Override
    public void run(String... args) {
        log.info("Koturno started");

        HGroup defaultHGroup = hGroupService.getGroupByName("default");
        if (defaultHGroup == null) {
            defaultHGroup = new HGroup("default", "Default group");
            hGroupService.save(defaultHGroup);
            log.info("Default host group created");
        }

        IGroup defaultIGroup = iGroupService.getGroupByName("default");
        if (defaultIGroup == null) {
            defaultIGroup = new IGroup("default", "Default group");
            iGroupService.save(defaultIGroup);
            log.info("Default inaccessibility group created");
        }

        List<Inaccessibility> inaccessibilities = inaccessibilityService.getAllInaccessibility();
        int oldActiveInaccessibilities = 0;
        for (Inaccessibility inaccessibility : inaccessibilities) {
            if (inaccessibility.getStart().equals(inaccessibility.getEnd())) {
                inaccessibility.setEnd(LocalDateTime.now());
                inaccessibility.setActive(false);
                oldActiveInaccessibilities++;
            }
            inaccessibilityService.save(inaccessibility);
        }

        if (oldActiveInaccessibilities > 0) {
            log.info("Clear {} old active inaccessibilities", oldActiveInaccessibilities);
        }

        log.info("Koturno initialization complete");
    }
}
