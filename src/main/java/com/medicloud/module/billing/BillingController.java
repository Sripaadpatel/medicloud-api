package com.medicloud.module.billing;

import com.medicloud.module.billing.dto.InvoiceCreateDTO;
import com.medicloud.module.billing.dto.InvoiceResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing") // Full path is /api/v1/billing
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')") // (Fulfills SRS 5.4 - only Admin/Staff)
public class BillingController {

    private final BillingService billingService;

    /**
     * Creates a new invoice. (SRS 4.4)
     */
    @PostMapping("/invoices")
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@Valid @RequestBody InvoiceCreateDTO invoiceDTO) {
        InvoiceResponseDTO createdInvoice = billingService.createInvoice(invoiceDTO);
        return new ResponseEntity<>(createdInvoice, HttpStatus.CREATED);
    }

    /**
     * Gets a single invoice by its ID.
     */
    @GetMapping("/invoices/{invoiceId}")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceById(@PathVariable Long invoiceId) {
        InvoiceResponseDTO invoice = billingService.getInvoiceById(invoiceId);
        return ResponseEntity.ok(invoice);
    }

    /**
     * Gets all invoices for a specific patient.
     */
    @GetMapping("/invoices/patient/{patientId}")
    public ResponseEntity<List<InvoiceResponseDTO>> getInvoicesForPatient(@PathVariable Long patientId) {
        List<InvoiceResponseDTO> invoices = billingService.getInvoicesForPatient(patientId);
        return ResponseEntity.ok(invoices);
    }

    // TODO: Add the payment processing endpoint
    // @PostMapping("/payments")
    // public ResponseEntity<?> processPayment(...) { ... }
}