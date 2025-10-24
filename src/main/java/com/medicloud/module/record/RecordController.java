package com.medicloud.module.record;

import com.medicloud.module.record.dto.RecordCreateDTO;
import com.medicloud.module.record.dto.RecordResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.medicloud.module.user.model.User; // Assuming User implements UserDetails
import org.springframework.security.access.AccessDeniedException; // For throwing 403
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
    public ResponseEntity<RecordResponseDTO> getRecordById(
            @PathVariable Long recordId, Authentication authentication) { // <-- Inject Authentication

        // --- TODO IMPLEMENTED ---
        User currentUser = (User) authentication.getPrincipal();
        RecordResponseDTO record = recordService.getRecordById(recordId); // Fetch the record first
        // If the user is a PATIENT, check if they own this record
        if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PATIENT"))
                && !record.getPatientId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Patients can only view their own medical records.");
        }
        // --- END OF IMPLEMENTATION ---

        return ResponseEntity.ok(record); // Return the fetched record
    }

    /**
     * Gets all medical records for a specific patient. (SRS 5.4)
     * PATIENT, DOCTOR, or ADMIN can view.
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<RecordResponseDTO>> getRecordsForPatient(
            @PathVariable Long patientId, Authentication authentication) { // <-- Inject Authentication

        // --- TODO IMPLEMENTED ---
        User currentUser = (User) authentication.getPrincipal();
        // If the user is a PATIENT, check if they are requesting their own records
        if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PATIENT"))
                && !currentUser.getId().equals(patientId)) {
            throw new AccessDeniedException("Patients can only view their own medical records.");
        }
        // --- END OF IMPLEMENTATION ---

        List<RecordResponseDTO> records = recordService.getRecordsForPatient(patientId);
        return ResponseEntity.ok(records);
    }
}