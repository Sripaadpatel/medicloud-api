package com.medicloud.shared.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

@Service // Mark this as the active implementation for now
public class MockPaymentGatewayService implements PaymentGatewayService {

    private static final Logger logger = LoggerFactory.getLogger(MockPaymentGatewayService.class);

    @Override
    public String processPayment(String paymentMethodNonce, BigDecimal amount, String currency) {
        // Simulate payment processing
        logger.info("--- SIMULATING PAYMENT ---");
        logger.info("Nonce: {}", paymentMethodNonce);
        logger.info("Amount: {} {}", amount, currency);

        // Simulate success/failure randomly or based on nonce
        if (paymentMethodNonce == null || paymentMethodNonce.contains("fail")) {
            logger.error("Payment failed!");
            throw new RuntimeException("Payment processor declined the transaction.");
        }

        String transactionId = "txn_" + UUID.randomUUID().toString();
        logger.info("Payment successful! Transaction ID: {}", transactionId);
        logger.info("--------------------------");
        return transactionId;
    }
}