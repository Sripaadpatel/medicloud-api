package com.medicloud.shared.notification;

import org.springframework.stereotype.Service;

@Service
public class SmsService {

    public void sendSms(String phoneNumber, String message) {
        // This is a mock.
        // In a real project, you would use the Twilio SDK or another SMS gateway API

        System.out.println("--- SIMULATING SMS ---");
        System.out.println("To: " + phoneNumber);
        System.out.println("Message: " + message);
        System.out.println("----------------------");
    }
}