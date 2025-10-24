package com.medicloud.module.record.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RecordResponseDTO {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private LocalDate visitDate;
    private String diagnosis;
    private String prescription;
    private String notes;
}