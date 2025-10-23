package com.medicloud.module.appointment;

import com.medicloud.module.appointment.dto.AppointmentRequestDTO;
import com.medicloud.module.appointment.dto.AppointmentResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
            @PathVariable Long patientId) {

        // TODO: Add a check to ensure a PATIENT can only see their *own* appointments

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
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId) {

        // TODO: Add a check to ensure a PATIENT can only cancel their *own* appointment

        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }
}