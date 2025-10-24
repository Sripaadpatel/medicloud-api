package com.medicloud.module.billing.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvoiceCreateDTO {
    @NotNull
    private Long patientId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    private LocalDate issueDate;

    @NotNull
    private LocalDate dueDate;

    private String serviceDescription;
}