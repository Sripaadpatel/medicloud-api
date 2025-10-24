package com.medicloud.module.record.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RecordCreateDTO {
    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotNull
    private LocalDate visitDate;

    private String diagnosis;
    private String prescription;
    private String notes;
}