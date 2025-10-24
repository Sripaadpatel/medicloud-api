package com.medicloud.module.record;

import com.medicloud.module.record.dto.RecordCreateDTO;
import com.medicloud.module.record.dto.RecordResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/records") // Full path is /api/v1/records
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    /**
     * Creates a new medical record. (SRS 4.3)
     * Only DOCTOR or ADMIN can create.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<RecordResponseDTO> createRecord(@Valid @RequestBody RecordCreateDTO recordDTO) {
        RecordResponseDTO savedRecord = recordService.createRecord(recordDTO);
        return new ResponseEntity<>(savedRecord, HttpStatus.CREATED);
    }

    /**
     * Gets a single medical record by its ID. (SRS 5.4)
     * PATIENT, DOCTOR, or ADMIN can view.
     */
    @GetMapping("/{recordId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<RecordResponseDTO> getRecordById(@PathVariable Long recordId) {

        // TODO: Add security check:
        // 1. Get the logged-in user's details.
        // 2. If user is PATIENT, check that record.patientId == loggedInUser.id

        RecordResponseDTO record = recordService.getRecordById(recordId);
        return ResponseEntity.ok(record);
    }

    /**
     * Gets all medical records for a specific patient. (SRS 5.4)
     * PATIENT, DOCTOR, or ADMIN can view.
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<RecordResponseDTO>> getRecordsForPatient(@PathVariable Long patientId) {

        // TODO: Add security check:
        // 1. Get the logged-in user's details.
        // 2. If user is PATIENT, check that patientId == loggedInUser.id

        List<RecordResponseDTO> records = recordService.getRecordsForPatient(patientId);
        return ResponseEntity.ok(records);
    }
}