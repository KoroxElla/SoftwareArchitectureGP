package com.example.part1.controller;

import com.example.part1.domain.Doctor;
import com.example.part1.repo.DoctorRepo;
import com.example.part1.service.DoctorService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import java.util.List;


@RestController
@RequestMapping("/doctors")
public class DoctorRestController {

    private final DoctorService doctorService;
    private final DoctorRepo doctorRepo;

    public DoctorRestController(DoctorRepo doctorRepo, DoctorService doctorService) {
        this.doctorRepo = doctorRepo;
        this.doctorService = doctorService;
    }

    // === Endpoint #8: List all doctors (GET /doctors) ===
    @GetMapping
    public List<Doctor> getAllDoctors() {
        // Returns all doctors from the database with HTTP 200 (OK)
        return doctorRepo.findAll();
    }

    // === Endpoint #9: Create a new doctor (POST /doctors) ===
    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
        // Validations are handled by JPA/Hibernate (e.g., @NotBlank in entity)
        Doctor saved = doctorRepo.save(doctor); // Persists the new doctor
        return ResponseEntity.status(HttpStatus.CREATED).body(saved); // HTTP 201 (Created)
    }

    // === Endpoint #10: Retrieve a doctor by ID (GET /doctors/{id}) ===
    @GetMapping("/{id}")
    public ResponseEntity<Object> getDoctorById(@PathVariable Long id) {
        Optional<Doctor> optionalDoctor = doctorRepo.findById(id);
        return optionalDoctor.isPresent()
                ? ResponseEntity.ok(optionalDoctor.get()) // HTTP 200 (OK)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found."); // HTTP 404
    }

    // === Endpoint #11: Update a doctor by ID (PUT /doctors/{id}) ===
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor updated) {
        Optional<Doctor> optionalDoctor = doctorRepo.findById(id);
        if (optionalDoctor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
        }

        Doctor existing = optionalDoctor.get();
        // Partial update: Only non-null fields are updated
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getSpecialisation() != null) existing.setSpecialisation(updated.getSpecialisation());
        // ... (other fields)

        return ResponseEntity.ok(doctorRepo.save(existing)); // HTTP 200 (OK)
    }

    // === Endpoint #12: Delete a doctor by ID (DELETE /doctors/{id}) ===
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDoctor(@PathVariable Long id) {
        boolean deleted = doctorService.deleteDoctorAndPreserveMedicalRecords(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
        }
        return ResponseEntity.ok("Doctor deleted and medical records preserved.");
    }


    // === Endpoint #13: List all appointments for a doctor (GET /doctors/{id}/appointments) ===
    @GetMapping("/{id}/appointments")
    public ResponseEntity<Object> getDoctorAppointments(@PathVariable Long id) {
        Optional<Doctor> doctor = doctorRepo.findById(id);
        return doctor.isPresent()
                ? ResponseEntity.ok(doctor.get().getAppointments()) // HTTP 200 (OK)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found."); // HTTP 404
    }
}
