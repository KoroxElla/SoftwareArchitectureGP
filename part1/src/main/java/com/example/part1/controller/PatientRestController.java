package com.example.part1.controller;

import com.example.part1.domain.Patient;
import com.example.part1.domain.Appointments;
import com.example.part1.domain.MedicalRecord;
import com.example.part1.repo.PatientRepo;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientRestController {

    private final PatientRepo patientRepo;

    public PatientRestController(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    // === Endpoint #1: List all patients (GET /patients) ===
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepo.findAll(); // Returns all patients from the database
    }

    // === Endpoint #2: Create a new patient (POST /patients) ===
    @PostMapping
    public ResponseEntity<Object> createPatient(@RequestBody Patient patient) {
        Patient saved = patientRepo.save(patient); // Saves the new patient to the database
        return ResponseEntity.status(HttpStatus.CREATED).body(saved); // Returns 201 (Created) with the saved patient
    }

    // === Endpoint #3: Retrieve a specific patient by ID (GET /patients/{id}) ===
    @GetMapping("/{id}")
    public ResponseEntity<Object> getPatientById(@PathVariable Long id) {
        Optional<Patient> optional = patientRepo.findById(id); // Queries the database for the patient

        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get()); // Returns 200 (OK) with the patient if found
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found."); // Returns 404 (Not Found) if patient doesn't exist
        }
    }

    // === Endpoint #4: Update a specific patient by ID (PUT /patients/{id}) ===
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePatient(@PathVariable Long id, @RequestBody Patient updated) {
        Optional<Patient> optional = patientRepo.findById(id);

        if (optional.isPresent()) {
            Patient existing = optional.get();
            // Updates only the non-null fields from the request
            existing.setName(updated.getName());
            existing.setEmail(updated.getEmail());
            existing.setPhoneNumber(updated.getPhoneNumber());
            existing.setAddress(updated.getAddress());
            return ResponseEntity.ok(patientRepo.save(existing)); // Returns 200 (OK) with the updated patient
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found."); // Returns 404 (Not Found) if patient doesn't exist
        }
    }

    // === Endpoint #5: Delete a specific patient by ID (DELETE /patients/{id}) ===
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePatient(@PathVariable Long id) {
        Optional<Patient> optional = patientRepo.findById(id);

        if (optional.isPresent()) {
            patientRepo.delete(optional.get()); // Deletes the patient (cascades to appointments and medical records)
            return ResponseEntity.ok("Patient deleted."); // Returns 200 (OK) with confirmation
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found."); // Returns 404 (Not Found) if patient doesn't exist
        }
    }

    // === Endpoint #6: List all appointments for a specific patient (GET /patients/{id}/appointments) ===
    @GetMapping("/{id}/appointments")
    public ResponseEntity<Object> getPatientAppointments(@PathVariable Long id) {
        Optional<Patient> optional = patientRepo.findById(id);

        if (optional.isPresent()) {
            // Returns all appointments linked to the patient (empty list if none exist)
            List<Appointments> appointments = optional.get().getAppointments();
            return ResponseEntity.ok(appointments);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found."); // Returns 404 if patient doesn't exist
        }
    }

    // === Endpoint #7: List all medical records for a specific patient (GET /patients/{id}/medical-records) ===
    @GetMapping("/{id}/medical-records")
    public ResponseEntity<Object> getPatientRecords(@PathVariable Long id) {
        Optional<Patient> optional = patientRepo.findById(id);

        if (optional.isPresent()) {
            // Collects all non-null medical records from the patient's appointments
            List<MedicalRecord> medicalRecords = optional.get().getAppointments().stream()
                    .map(Appointments::getMedicalRecord) // Extracts medical records
                    .filter(java.util.Objects::nonNull) // Filters out null records (if any)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(medicalRecords); // Returns 200 (OK) with the list
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found."); // Returns 404 if patient doesn't exist
        }
    }
}