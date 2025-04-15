package com.example.part1.controller;

import com.example.part1.domain.Doctor;
import com.example.part1.domain.Appointments;
import com.example.part1.repo.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import java.util.List;


@RestController
@RequestMapping("/doctors")
public class DoctorRestController {
    @Autowired
    private DoctorRepo doctorRepo;

    // Endpoint #8 -  Handle get requests to fetch all doctors
    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }

    // Endpoint #9 - Handle post request to create a new doctor
    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
        Doctor saved = doctorRepo.save(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    //  Endpoint #10 - handle get request to fetch doctors by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getDoctorById(@PathVariable Long id) {
        Optional<Doctor> optionalDoctor = doctorRepo.findById(id);
        if (optionalDoctor.isPresent()) {
            return ResponseEntity.ok(optionalDoctor.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
        }
    }

    // Endpoint #11 - Handle put requests to update doctor by ID
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDoctor(@PathVariable Long id, @RequestBody Doctor updated) {
        Optional<Doctor> optionalDoctor = doctorRepo.findById(id);
        if (optionalDoctor.isPresent()) {
            Doctor existing = optionalDoctor.get();
            existing.setName(updated.getName());
            existing.setSpecialisation(updated.getSpecialisation());
            existing.setEmail(updated.getEmail());
            existing.setPhoneNumber(updated.getPhoneNumber());
            Doctor saved = doctorRepo.save(existing);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
        }
    }

    // Endpoint #12 - Handle delete doctor requests by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDoctor(@PathVariable Long id) {
        Optional<Doctor> optionalDoctor = doctorRepo.findById(id);
        if (optionalDoctor.isPresent()) {
            doctorRepo.delete(optionalDoctor.get());
            return ResponseEntity.ok("Doctor deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
        }
    }

    // Endpoint #13 - Handle get request to fetch all appointments for a doctor
    @GetMapping("/{id}/appointments")
    public ResponseEntity<Object> getDoctorAppointments(@PathVariable Long id) {
        Optional<Doctor> optionalDoctor = doctorRepo.findById(id);
        if (optionalDoctor.isPresent()) {
            List<Appointments> appointments = optionalDoctor.get().getAppointments();
            return ResponseEntity.ok(appointments);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
        }
    }
}
