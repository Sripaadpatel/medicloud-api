package com.medicloud.shared.payment;

import com.medicloud.module.billing.dto.PaymentRequestDTO;
import java.math.BigDecimal;

public interface PaymentGatewayService {
    // Returns a transaction ID or throws an exception on failure
    String processPayment(String paymentMethodNonce, BigDecimal amount, String currency);
}