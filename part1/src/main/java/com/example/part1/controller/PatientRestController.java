package com.example.part1.controller;

import com.example.part1.domain.Patient;
import com.example.part1.domain.Appointments;
import com.example.part1.domain.MedicalRecord;
import com.example.part1.repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;


@RestController
@RequestMapping("/patients")
public class PatientRestController {
    @Autowired
    private PatientRepo patientRepo;

    // Endpoint #1 - Handles get requests to fetch all patients
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    // Endpoint #2 - Handle post requests to create a patient
    @PostMapping
    public ResponseEntity<Object> createPatient(@RequestBody Patient patient) {
        Patient saved = patientRepo.save(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Endpoint #3 - Handle get requests to fetch patients by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getPatientById(@PathVariable Long id) {
        Optional<Patient> optional = patientRepo.findById(id);

        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
        }
    }

    // Endpoint #4 - Handle put requests to update patients details by id
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePatient(@PathVariable Long id, @RequestBody Patient updated) {
        Optional<Patient> optional = patientRepo.findById(id);
        if (optional.isPresent()) {
            Patient existing = optional.get();
            existing.setName(updated.getName());
            existing.setEmail(updated.getEmail());
            existing.setPhoneNumber(updated.getPhoneNumber());
            existing.setAddress(updated.getAddress());
            return ResponseEntity.ok(patientRepo.save(existing));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
        }
    }

    // Endpoint #5 - Handle delete patient request by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePatient(@PathVariable Long id) {
        Optional<Patient> optional = patientRepo.findById(id);
        if (optional.isPresent()) {
            patientRepo.delete(optional.get());
            return ResponseEntity.ok("Patient deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
        }
    }

    // Endpoint #6 -  Handle gwt requests to fetch all appointments for a specific patient, filtering them by id
    @GetMapping("/{id}/appointments")
    public ResponseEntity<Object> getPatientAppointments(@PathVariable Long id) {
        Optional<Patient> optional = patientRepo.findById(id);
        if (optional.isPresent()) {
            List<Appointments> appointments = optional.get().getAppointments();
            return ResponseEntity.ok(appointments);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
        }
    }

    //Endpoint #7 -  Handle get requests to fetch all medical records for a specific patient
    @GetMapping("/{id}/medical-records")
    public ResponseEntity<Object> getPatientRecords(@PathVariable Long id) {
        Optional<Patient> optional = patientRepo.findById(id);
        if (optional.isPresent()) {
            List<MedicalRecord> records = optional.get().getAppointments().stream()
                    .map(Appointments::getRecord)
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(records);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
        }
    }
}
