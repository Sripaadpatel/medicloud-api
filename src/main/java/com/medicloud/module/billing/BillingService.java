package com.medicloud.module.billing;

import com.medicloud.exception.ResourceNotFoundException;
import com.medicloud.module.billing.dto.InvoiceCreateDTO;
import com.medicloud.module.billing.dto.InvoiceResponseDTO;
import com.medicloud.module.billing.model.Invoice;
import com.medicloud.module.billing.model.InvoiceStatus;
import com.medicloud.module.billing.repository.InvoiceRepository;
import com.medicloud.module.user.model.User;
import com.medicloud.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    // In a real app, you would also inject a PaymentGatewayService here

    @Transactional
    public InvoiceResponseDTO createInvoice(InvoiceCreateDTO dto) {
        // (Fulfills SRS 5.4 - An invoice must be generated)
        User patient = userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getPatientId()));

        Invoice invoice = new Invoice();
        invoice.setPatient(patient);
        invoice.setAmount(dto.getAmount());
        invoice.setIssueDate(dto.getIssueDate());
        invoice.setDueDate(dto.getDueDate());
        invoice.setServiceDescription(dto.getServiceDescription());
        invoice.setStatus(InvoiceStatus.ISSUED); // Default status

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(savedInvoice);
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponseDTO> getInvoicesForPatient(Long patientId) {
        if (!userRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("User", "id", patientId);
        }
        List<Invoice> invoices = invoiceRepository.findByPatientId(patientId);
        return invoices.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InvoiceResponseDTO getInvoiceById(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", invoiceId));
        return convertToDTO(invoice);
    }

    // TODO: Add a method to process payments (SRS 4.4)
    // public InvoiceResponseDTO processPayment(Long invoiceId, PaymentRequestDTO dto) { ... }

    // --- Helper Method to convert Entity to DTO ---
    private InvoiceResponseDTO convertToDTO(Invoice invoice) {
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
        dto.setId(invoice.getId());
        dto.setAmount(invoice.getAmount());
        dto.setIssueDate(invoice.getIssueDate());
        dto.setDueDate(invoice.getDueDate());
        dto.setStatus(invoice.getStatus());
        dto.setServiceDescription(invoice.getServiceDescription());

        if (invoice.getPatient() != null) {
            dto.setPatientId(invoice.getPatient().getId());
            dto.setPatientName(invoice.getPatient().getFirstName() + " " + invoice.getPatient().getLastName());
        }
        return dto;
    }
}