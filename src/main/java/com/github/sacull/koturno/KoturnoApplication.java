package com.github.sacull.koturno;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.services.HGroupService;
import com.github.sacull.koturno.services.HostService;
import com.github.sacull.koturno.services.InaccessibilityService;
import com.github.sacull.koturno.utils.BackgroundChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executor;

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

		HGroup defaultGroup = hGroupService.getGroupByName("default");
		if (defaultGroup == null) {
			defaultGroup = new HGroup("default", "Default group");
			hGroupService.save(defaultGroup);
			log.info("Default host group created");
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
			log.info("Clear {} old active inaccessibilities",oldActiveInaccessibilities);
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
