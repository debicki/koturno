package com.github.sacull.koturno;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.services.HGroupService;
import com.github.sacull.koturno.services.HostService;
import com.github.sacull.koturno.services.InaccessibilityService;
import com.github.sacull.koturno.utils.BackgroundChecker;
import com.github.sacull.koturno.utils.FileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableAsync
@Slf4j(topic = "KoturnoApplication")
public class KoturnoApplication implements CommandLineRunner {

	private HostService hostService;
	private HGroupService hGroupService;
	private InaccessibilityService inaccessibilityService;
	private BackgroundChecker backgroundChecker;

	@Autowired
	public KoturnoApplication(HostService hostService,
							  HGroupService hGroupService,
							  InaccessibilityService inaccessibilityService,
							  BackgroundChecker backgroundChecker) {
		this.hostService = hostService;
		this.hGroupService = hGroupService;
		this.inaccessibilityService = inaccessibilityService;
		this.backgroundChecker = backgroundChecker;
	}

	public static void main(String[] args) {
		SpringApplication.run(KoturnoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("Koturno started");
		List<Host> hostsToAdd = new ArrayList<>();
		List<Host> hostsInDatabase = hostService.getAllHosts();
		List<HGroup> groupsToUpdate = new ArrayList<>();
		HGroup defaultGroup = hGroupService.getGroupByName("default");
		if (defaultGroup == null) {
			defaultGroup = new HGroup("default", "Default group");
			defaultGroup = hGroupService.save(defaultGroup);
		}
		if (args.length >= 2) {
			if (args[0].equalsIgnoreCase("-f")) {
				try {
					hostsToAdd = FileManager.loadHosts(args[1]);
					for (Host host : hostsInDatabase) {
						hostsToAdd = hostsToAdd.stream().filter(x -> !x.compareAddress(host)).collect(Collectors.toList());
					}
					for (Host host : hostsToAdd) {
						if (host.getName().equals("") || host.getName() == null) {
							host.setHostGroup(defaultGroup);
						} else {
							HGroup group = hGroupService.getGroupByName(host.getName());
							if (group == null) {
								group = new HGroup(host.getName(), "");
								group = hGroupService.save(group);
							}
							host.setHostGroup(group);
						}
					}
					log.info("{} hosts from file {} was added", hostsToAdd.size(), args[1]);
				} catch (IOException ex) {
					log.error("Error while loading hosts from file");
				}
			} else {
				log.error("Not recognized parameter");
			}
		} else if (args.length == 1 && args[0].equalsIgnoreCase("-X")) {
			log.info("Nothing to do");
		} else if (args.length == 1) {
			log.error("Incomplete parameter");
		}

		if (hostsToAdd.size() > 0) {
			for (Host host : hostsToAdd) {
				hostService.save(host);
			}
		}

		List<Inaccessibility> inaccessibilities = inaccessibilityService.getAllInaccessibility();
		for (Inaccessibility inaccessibility : inaccessibilities) {
			if (inaccessibility.getStart().equals(inaccessibility.getEnd())) {
				inaccessibility.setEnd(LocalDateTime.now());
				inaccessibility.setActive(false);
			}
			inaccessibilityService.save(inaccessibility);
		}

		backgroundChecker.start();
	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("Checker-");
		executor.initialize();
		return executor;
	}
}
