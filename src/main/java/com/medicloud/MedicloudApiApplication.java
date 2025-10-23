package com.medicloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.medicloud.module.user.model.Role;
import com.medicloud.module.user.model.User;
import com.medicloud.module.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;

@SpringBootApplication
public class MedicloudApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicloudApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create a test patient if one doesn't exist
            if (userRepository.findByEmail("testpatient@medicloud.com").isEmpty()) {
                System.out.println("--- CREATING TEST PATIENT ---");
                User patient = new User();
                patient.setEmail("testpatient@medicloud.com");
                patient.setPassword(passwordEncoder.encode("password123"));
                patient.setFirstName("Test");
                patient.setLastName("Patient");
                patient.setRoles(Set.of(Role.ROLE_PATIENT));
                userRepository.save(patient);
                System.out.println("--- TEST PATIENT CREATED (ID: " + patient.getId() + ") ---");
            }

            // Create a test doctor if one doesn't exist
            if (userRepository.findByEmail("doctor@medicloud.com").isEmpty()) {
                System.out.println("--- CREATING TEST DOCTOR ---");
                User doctor = new User();
                doctor.setEmail("doctor@medicloud.com");
                doctor.setPassword(passwordEncoder.encode("password123"));
                doctor.setFirstName("Dr.");
                doctor.setLastName("House");
                doctor.setRoles(Set.of(Role.ROLE_DOCTOR));
                userRepository.save(doctor);
                System.out.println("--- TEST DOCTOR CREATED (ID: " + doctor.getId() + ") ---");
            }
        };
    }
    // --- END OF NEW CODE BLOCK ---

}
