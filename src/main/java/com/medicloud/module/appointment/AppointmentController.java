package com.medicloud.module.appointment;

import com.medicloud.module.appointment.dto.AppointmentRequestDTO;
import com.medicloud.module.appointment.dto.AppointmentResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.medicloud.module.user.model.User;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/appointments") // Full path is /api/v1/appointments
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * Creates a new appointment. (SRS 4.2)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'STAFF', 'ADMIN')")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @Valid @RequestBody AppointmentRequestDTO requestDTO) {

        AppointmentResponseDTO createdAppointment = appointmentService.scheduleAppointment(requestDTO);
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    /**
     * Gets all appointments for a specific patient. (SRS 5.4)
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsForPatient(
            @PathVariable Long patientId, Authentication authentication) { // <-- Inject Authentication

        // --- TODO IMPLEMENTED ---
        User currentUser = (User) authentication.getPrincipal();
        // If the logged-in user is a PATIENT, they can only access their own appointments
        if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PATIENT"))
                && !currentUser.getId().equals(patientId)) {
            throw new AccessDeniedException("Patients can only view their own appointments.");
        }
        // --- END OF IMPLEMENTATION ---

        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Gets all appointments for a specific doctor. (SRS 5.4)
     */
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsForDoctor(
            @PathVariable Long doctorId) {

        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Cancels an appointment. (SRS 4.2)
     */
    @DeleteMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'STAFF', 'ADMIN')")
    public ResponseEntity<Void> cancelAppointment(
            @PathVariable Long appointmentId, Authentication authentication) { // <-- Inject Authentication

        // --- TODO IMPLEMENTED ---
        User currentUser = (User) authentication.getPrincipal();
        // If the logged-in user is a PATIENT, they can only cancel their own appointments
        // (We need to fetch the appointment first to check its patientId)
        if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PATIENT"))) {
            AppointmentResponseDTO appointment = appointmentService.getAppointmentById(appointmentId); // You'll need to add this method to the service
            if (!appointment.getPatientId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Patients can only cancel their own appointments.");
            }
        }
        // --- END OF IMPLEMENTATION ---

        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }
}