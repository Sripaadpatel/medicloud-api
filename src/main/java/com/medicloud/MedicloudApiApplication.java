package com.medicloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Delete the other imports related to CommandLineRunner, User, Role, etc.

@SpringBootApplication
public class MedicloudApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicloudApiApplication.class, args);
    }

    // The @Bean CommandLineRunner method is now GONE.

}