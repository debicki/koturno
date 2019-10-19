package com.github.sacull.koturno;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.repositories.HGroupRepository;
import com.github.sacull.koturno.repositories.HostRepository;
import com.github.sacull.koturno.services.BackgroundChecker;
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

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
	private EntityManager em;

	@Autowired
	private BackgroundChecker backgroundChecker;

	private Logger logger = LoggerFactory.getLogger("KoturnoApplication");

	public static void main(String[] args) {
		SpringApplication.run(KoturnoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Koturno started");
		List<Host> hostsToAdd = new ArrayList<>();
		List<Host> hostsInDatabase = hostRepository.getAllHosts();
		List<HGroup> hostsGroupsList = hGroupRepository.getAllGroups();
		List<HGroup> groupsToUpdate = new ArrayList<>();
		HGroup defaultGroup = hGroupRepository.getDefaultHostGroup();
		if (args.length >= 2) {
			if (args[0].equalsIgnoreCase("-f")) {
				try {
					hostsToAdd = FileManager.loadHosts(args[1]);
					for (Host host : hostsInDatabase) {
						hostsToAdd = hostsToAdd.stream().filter(x -> !x.compareIPv4(host)).collect(Collectors.toList());
					}
					for (Host host : hostsToAdd) {
						if (host.getHostname().equals("") || host.getHostname() == null) {
							host.setHostGroup(defaultGroup);
						} else {
							HGroup group = hGroupRepository.getByName(host.getHostname());
							if (group == null) {
								group = new HGroup(host.getHostname(), "", new ArrayList<>());
							}
							host.setHostGroup(group);
							if (!groupsToUpdate.contains(group)) {
								groupsToUpdate.add(group);
							}
						}
					}
					logger.info("{} hosts from file {} was added", hostsToAdd.size(), args[1]);
				} catch (IOException ex) {
					logger.error("Error while loading hosts from file");
				}
			} else if (args[0].equalsIgnoreCase("-a")) {
				Host hostToAdd;
				boolean isFound;
				for (int i = 1; i < args.length; i++) {
					isFound = false;
					hostToAdd = new Host("",
							args[i],
							"",
							defaultGroup,
							new ArrayList<>());
					for (Host host : hostsInDatabase) {
						if (host.compareIPv4(hostToAdd)) {
							isFound = true;
						}
					}
					if (!isFound) {
						hostsToAdd.add(hostToAdd);
						if (!groupsToUpdate.contains(defaultGroup)) {
							groupsToUpdate.add(defaultGroup);
						}
						logger.info("Host {} added", args[i]);
					} else {
						logger.info("Host {} isn't added, because database contains that IPv4 address", args[i]);
					}
				}
			} else {
				logger.error("Not recognized parameter");
			}
		} else if (args.length == 1) {
			logger.error("Incomplete parameter");
		}

		if (groupsToUpdate.size() > 0) {
			for (HGroup group : groupsToUpdate) {
				hGroupRepository.save(group);
			}
		}
		if (hostsToAdd.size() > 0) {
			for (Host host : hostsToAdd) {
				hostRepository.save(host);
			}
		}

		CompletableFuture<String> firstChecker = backgroundChecker.start();
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
