package com.medicloud.module.appointment.repository;

import com.medicloud.module.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Find appointments for a specific patient
    List<Appointment> findByPatientId(Long patientId);

    // Find appointments for a specific doctor
    List<Appointment> findByDoctorId(Long doctorId);
}