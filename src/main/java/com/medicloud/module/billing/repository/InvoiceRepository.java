package com.medicloud.module.billing.repository;

import com.medicloud.module.billing.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // Find all invoices for a specific patient
    List<Invoice> findByPatientId(Long patientId);
}