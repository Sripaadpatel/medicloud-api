package com.medicloud.shared.notification;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendEmail(String to, String subject, String body) {
        // This is a mock.
        // In a real project, you would use JavaMailSender
        // and configure SMTP properties in application.properties

        System.out.println("--- SIMULATING EMAIL ---");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("------------------------");
    }
}