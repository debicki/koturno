package com.github.sacull.koturno;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.HGroupRepository;
import com.github.sacull.koturno.repositories.HostRepository;
import com.github.sacull.koturno.repositories.InaccessibilityRepository;
import com.github.sacull.koturno.utils.BackgroundChecker;
import com.github.sacull.koturno.utils.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class KoturnoApplication implements CommandLineRunner {

	@Autowired
	private HostRepository hostRepository;

	@Autowired
	private HGroupRepository hGroupRepository;

	@Autowired
	private InaccessibilityRepository inaccessibilityRepository;

	@Autowired
	private BackgroundChecker backgroundChecker;

	private Logger logger = LoggerFactory.getLogger("KoturnoApplication");

	public static void main(String[] args) {
		SpringApplication.run(KoturnoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		logger.info("Koturno started");
		List<Host> hostsToAdd = new ArrayList<>();
		List<Host> hostsInDatabase = hostRepository.findAll();
		List<HGroup> groupsToUpdate = new ArrayList<>();
		HGroup defaultGroup = hGroupRepository.findByName("default");
		if (defaultGroup == null) {
			defaultGroup = new HGroup("default", "Default group");
			defaultGroup = hGroupRepository.save(defaultGroup);
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
							HGroup group = hGroupRepository.findByName(host.getName());
							if (group == null) {
								group = new HGroup(host.getName(), "");
								group = hGroupRepository.save(group);
							}
							host.setHostGroup(group);
						}
					}
					logger.info("{} hosts from file {} was added", hostsToAdd.size(), args[1]);
				} catch (IOException ex) {
					logger.error("Error while loading hosts from file");
				}
			} else {
				logger.error("Not recognized parameter");
			}
		} else if (args.length == 1 && args[0].equalsIgnoreCase("-X")) {
			logger.info("Nothing to do");
		} else if (args.length == 1) {
			logger.error("Incomplete parameter");
		}

		if (hostsToAdd.size() > 0) {
			for (Host host : hostsToAdd) {
				hostRepository.save(host);
			}
		}

		List<Inaccessibility> inaccessibilities = inaccessibilityRepository.findAll();
		for (Inaccessibility inaccessibility : inaccessibilities) {
			if (inaccessibility.getStart().equals(inaccessibility.getEnd())) {
				inaccessibility.setEnd(LocalDateTime.now());
				inaccessibility.setActive(false);
			}
			inaccessibilityRepository.save(inaccessibility);
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
