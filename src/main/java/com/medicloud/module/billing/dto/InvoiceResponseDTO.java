package com.medicloud.module.billing.dto;

import com.medicloud.module.billing.model.InvoiceStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvoiceResponseDTO {
    private Long id;
    private Long patientId;
    private String patientName;
    private BigDecimal amount;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private InvoiceStatus status;
    private String serviceDescription;
}