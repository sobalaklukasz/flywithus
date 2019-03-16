package com.flywithus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@EnableScheduling
@ActiveProfiles("prod")
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}
