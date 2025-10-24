package com.medicloud.module.record.model;

import com.medicloud.module.user.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "medical_records")
@Data
@NoArgsConstructor
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @Column(nullable = false)
    private LocalDate visitDate;

    @Lob // Large Object, for long text
    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String prescription;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String notes;
}