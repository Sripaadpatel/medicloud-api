package com.medicloud.module.appointment.dto;

import com.medicloud.module.appointment.model.AppointmentStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentResponseDTO {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private LocalDateTime appointmentTime;
    private String reason;
    private AppointmentStatus status;
}