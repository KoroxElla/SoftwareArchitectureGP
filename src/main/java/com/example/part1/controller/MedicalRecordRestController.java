package com.example.part1.controller;

import com.example.part1.domain.Appointments;
import com.example.part1.domain.Record;
import com.example.part1.repo.AppointmentRepo;
import com.example.part1.repo.RecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


@RestController
@RequestMapping("/medical-records")
public class MedicalRecordRestController {
    @Autowired
    private RecordRepo recordRepo;

    @Autowired
    private AppointmentRepo appointmentRepo;


    // Endpoint #20 - Handles post request to create a new medical record linked to an existing appointment
    @PostMapping
    public ResponseEntity<Object> createMedicalRecord(@RequestBody Record medicalRecord) {
        // Ensure the appointment exist and has a valid ID
        if (medicalRecord.getAppointment() == null || medicalRecord.getAppointment().getId() == null) {
            return ResponseEntity.badRequest().body("Appointment is required.");
        }
        Optional<Appointments> optional = appointmentRepo.findById(medicalRecord.getAppointment().getId());
        //If appointment does not exist
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
        }

        // Link existing appointment to the record
        medicalRecord.setAppointment(optional.get());
        // Save the medical record
        Record saved = recordRepo.save(medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
