package com.example.part1.controller;

import com.example.part1.domain.Appointments;
import com.example.part1.domain.MedicalRecord;
import com.example.part1.repo.AppointmentRepo;
import com.example.part1.repo.RecordRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/medical-records")
public class MedicalRecordRestController {

    private final RecordRepo recordRepo;
    private final AppointmentRepo appointmentRepo;

    public MedicalRecordRestController(RecordRepo recordRepo, AppointmentRepo appointmentRepo) {
        this.recordRepo = recordRepo;
        this.appointmentRepo = appointmentRepo;
    }

    // === Endpoint #20: Create a medical record (POST /medical-records) ===
    @PostMapping
    public ResponseEntity<Object> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        // Set creation timestamp
        medicalRecord.setRecordDate(Timestamp.valueOf(LocalDateTime.now()));

        // Handle case when appointment is provided
        if (medicalRecord.getAppointment() != null && medicalRecord.getAppointment().getId() != null) {
            // Validate appointment linkage
            Optional<Appointments> appointment = appointmentRepo.findById(medicalRecord.getAppointment().getId());
            if (appointment.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found."); // HTTP 404
            }
            if (appointment.get().getMedicalRecord() != null) {
                return ResponseEntity.badRequest().body("Appointment already has a record."); // HTTP 400
            }

            // Link and save
            medicalRecord.setAppointment(appointment.get());
        }

        // Validate required medical record fields
        if (medicalRecord.getDiagnosis() == null || medicalRecord.getDiagnosis().isBlank()) {
            return ResponseEntity.badRequest().body("Diagnosis is required."); // HTTP 400
        }

        MedicalRecord saved = recordRepo.save(medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved); // HTTP 201
    }
}