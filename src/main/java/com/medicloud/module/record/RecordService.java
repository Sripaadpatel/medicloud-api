package com.medicloud.module.record;

import com.medicloud.exception.ResourceNotFoundException;
import com.medicloud.module.record.dto.RecordCreateDTO;
import com.medicloud.module.record.dto.RecordResponseDTO;
import com.medicloud.module.record.model.MedicalRecord;
import com.medicloud.module.record.repository.RecordRepository;
import com.medicloud.module.user.model.User;
import com.medicloud.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    @Transactional
    public RecordResponseDTO createRecord(RecordCreateDTO dto) {
        User patient = userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getPatientId()));

        User doctor = userRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getDoctorId()));

        MedicalRecord record = new MedicalRecord();
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setVisitDate(dto.getVisitDate());
        record.setDiagnosis(dto.getDiagnosis());
        record.setPrescription(dto.getPrescription());
        record.setNotes(dto.getNotes());

        MedicalRecord savedRecord = recordRepository.save(record);
        return convertToDTO(savedRecord);

    }

    @Transactional(readOnly = true)
    public RecordResponseDTO getRecordById(Long recordId) {
        MedicalRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalRecord", "id", recordId));
        return convertToDTO(record);
    }

    @Transactional(readOnly = true)
    public List<RecordResponseDTO> getRecordsForPatient(Long patientId) {
        if (!userRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("User", "id", patientId);
        }
        List<MedicalRecord> records = recordRepository.findByPatientId(patientId);
        return records.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // --- Helper Method to convert Entity to DTO ---
    private RecordResponseDTO convertToDTO(MedicalRecord record) {
        RecordResponseDTO dto = new RecordResponseDTO();
        dto.setId(record.getId());
        dto.setVisitDate(record.getVisitDate());
        dto.setDiagnosis(record.getDiagnosis());
        dto.setPrescription(record.getPrescription());
        dto.setNotes(record.getNotes());

        if (record.getPatient() != null) {
            dto.setPatientId(record.getPatient().getId());
            dto.setPatientName(record.getPatient().getFirstName() + " " + record.getPatient().getLastName());
        }
        if (record.getDoctor() != null) {
            dto.setDoctorId(record.getDoctor().getId());
            dto.setDoctorName(record.getDoctor().getFirstName() + " " + record.getDoctor().getLastName());
        }
        return dto;
    }
}