package com.medicloud.module.appointment;

import com.medicloud.exception.ResourceNotFoundException;
import com.medicloud.module.appointment.dto.AppointmentRequestDTO;
import com.medicloud.module.appointment.dto.AppointmentResponseDTO;
import com.medicloud.module.appointment.model.Appointment;
import com.medicloud.module.appointment.model.AppointmentStatus;
import com.medicloud.module.appointment.repository.AppointmentRepository;
import com.medicloud.module.user.model.User;
import com.medicloud.module.user.repository.UserRepository;
import com.medicloud.shared.notification.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    // You would also inject SmsService here (from SRS 3.3)

    @Transactional
    public AppointmentResponseDTO scheduleAppointment(AppointmentRequestDTO dto) {
        // Find the patient and doctor from the database
        User patient = userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getPatientId()));

        User doctor = userRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getDoctorId()));

        // Create the new Appointment entity
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setReason(dto.getReason());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        // (Fulfills SRS 3.3 - Send notification)
        emailService.sendEmail(
                patient.getEmail(),
                "Appointment Confirmed",
                "Your appointment with Dr. " + doctor.getLastName() + " is confirmed for " + dto.getAppointmentTime()
        );

        return convertToDTO(savedAppointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAppointmentsByPatient(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    @Transactional(readOnly = true) // Add this method
    public AppointmentResponseDTO getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));
        return convertToDTO(appointment);
    }
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAppointmentsByDoctor(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return appointments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        // (Notify patient and doctor)
        emailService.sendEmail(
                appointment.getPatient().getEmail(),
                "Appointment Cancelled",
                "Your appointment on " + appointment.getAppointmentTime() + " has been cancelled."
        );
    }

    // --- Helper Method to convert Entity to DTO ---
    // This prevents leaking the User entity (with hashed password!) to the frontend
    private AppointmentResponseDTO convertToDTO(Appointment appointment) {
        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        dto.setId(appointment.getId());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setReason(appointment.getReason());
        dto.setStatus(appointment.getStatus());

        // Handle potential null users if relations are lazy/optional
        if (appointment.getPatient() != null) {
            dto.setPatientId(appointment.getPatient().getId());
            dto.setPatientName(appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName());
        }
        if (appointment.getDoctor() != null) {
            dto.setDoctorId(appointment.getDoctor().getId());
            dto.setDoctorName(appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName());
        }
        return dto;
    }
}