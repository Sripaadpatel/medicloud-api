package com.medicloud.module.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequestDTO {
    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotNull @Future
    private LocalDateTime appointmentTime;

    private String reason;
}