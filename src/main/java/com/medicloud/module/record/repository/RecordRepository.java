package com.medicloud.module.record.repository;

import com.medicloud.module.record.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<MedicalRecord, Long> {

    // Find all records for a specific patient
    List<MedicalRecord> findByPatientId(Long patientId);
}