package com.medicloud.module.billing.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {
    @NotNull
    private Long invoiceId;
    // Example fields for a payment gateway
    @NotEmpty
    private String paymentMethodNonce; // e.g., from Stripe.js or Braintree
    @NotNull
    private BigDecimal amount; // Optional: verify amount matches invoice
}