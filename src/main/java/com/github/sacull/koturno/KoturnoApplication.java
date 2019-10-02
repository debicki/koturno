package com.github.sacull.koturno;

import com.github.sacull.koturno.entities.Host;
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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class KoturnoApplication implements CommandLineRunner {

	@Autowired
	HostRepository hostRepository;

	@Autowired
	BackgroundChecker backgroundChecker;

	Logger logger = LoggerFactory.getLogger("KoturnoApplication");

	public static void main(String[] args) {
		SpringApplication.run(KoturnoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Koturno started");
		List<Host> hostsToAdd = new ArrayList<>();
		if (args.length >= 2) {
			if (args[0].equalsIgnoreCase("-f")) {
				try {
					hostsToAdd = FileManager.loadHosts(args[1]);
					logger.info("Hosts from file {} added", args[1]);
				} catch (IOException ex) {
					logger.error("Error while loading hosts from file");
				}
			} else if (args[0].equalsIgnoreCase("-a")) {
				try {
					for (int i = 1; i < args.length; i++) {
						hostsToAdd.add(new Host(InetAddress.getByName(args[i]), null, null));
						logger.info("Host {} added", args[i]);
					}
				} catch (UnknownHostException e) {
					logger.error("Error while parsing host");
				}
			} else {
				logger.error("Not recognized parameter");
			}
		} else if (args.length == 1) {
			logger.error("Incomplete parameter");
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
