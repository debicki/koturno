package com.github.sacull.koturno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KoturnoApplication {

    public static void main(String[] args) {
        SpringApplication.run(KoturnoApplication.class, args);
    }
}
