package com.example.part1.controller;

import com.example.part1.domain.Appointments;
import com.example.part1.domain.Doctor;
import com.example.part1.domain.Patient;
import com.example.part1.domain.Record;
import com.example.part1.repo.AppointmentRepo;
import com.example.part1.repo.DoctorRepo;
import com.example.part1.repo.PatientRepo;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
public class AppointmentRestController {

    private final AppointmentRepo appointmentRepo;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;

    // Constructor injection for required repositories
    public AppointmentRestController(AppointmentRepo appointmentRepo, DoctorRepo doctorRepo, PatientRepo patientRepo) {
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
    }

    /**
     * Endpoint #14
     * Retrieve a list of all appointments.
     * GET /appointments
     */
    @GetMapping
    public List<Appointments> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    /**
     * Endpoint #15
     * Create a new appointment.
     * POST /appointments
     */
    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointments appointment) {
        // Ensure doctor and patient are provided
        if (appointment.getPatient() == null || appointment.getDoctor() == null) {
            return ResponseEntity.badRequest().body("Doctor and Patient must be provided.");
        }

        // Validate existence of doctor and patient in the database
        Optional<Patient> patient = patientRepo.findById(appointment.getPatient().getId());
        Optional<Doctor> doctor = doctorRepo.findById(appointment.getDoctor().getId());

        if (patient.isEmpty() || doctor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient or doctor not found.");
        }

        // Validate required fields for appointment creation
        if (appointment.getAppointmentDate() == null) {
            return ResponseEntity.badRequest().body("Appointment date must be provided.");
        }
        if (appointment.getStatus() == null) {
            return ResponseEntity.badRequest().body("Appointment status must be provided.");
        }
        if (appointment.getNotes() == null) {
            return ResponseEntity.badRequest().body("Notes must be provided.");
        }

        // Set managed patient and doctor instances
        appointment.setPatient(patient.get());
        appointment.setDoctor(doctor.get());

        // Save new appointment and return created entity
        Appointments saved = appointmentRepo.save(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Endpoint #16
     * Retrieve a specific appointment by ID.
     * GET /appointments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        Optional<Appointments> optionalAppointment = appointmentRepo.findById(id);

        // Return appointment if found, else return 404
        if (optionalAppointment.isPresent()) {
            return ResponseEntity.ok(optionalAppointment.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
        }
    }

    /**
     * Endpoint #17
     * Update an existing appointment by ID.
     * PUT /appointments/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody Appointments updated) {
        Optional<Appointments> optionalAppointment = appointmentRepo.findById(id);

        if (optionalAppointment.isPresent()) {
            Appointments existing = optionalAppointment.get();

            // Update modifiable fields
            existing.setAppointmentDate(updated.getAppointmentDate());
            existing.setStatus(updated.getStatus());
            existing.setNotes(updated.getNotes());

            // Save updated appointment and return it
            Appointments saved = appointmentRepo.save(existing);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
        }
    }

    /**
     * Endpoint #18
     * Delete an appointment by ID.
     * DELETE /appointments/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        Optional<Appointments> optionalAppointment = appointmentRepo.findById(id);

        // Delete if found, else return 404
        if (optionalAppointment.isPresent()) {
            appointmentRepo.delete(optionalAppointment.get());
            return ResponseEntity.ok("Appointment deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
        }
    }

    /**
     * Endpoint #19
     * Retrieve the medical record associated with a specific appointment.
     * GET /appointments/{id}/medical-record
     */
    @GetMapping("/{id}/medical-record")
    public ResponseEntity<?> getAppointmentRecord(@PathVariable Long id) {
        Optional<Appointments> appointment = appointmentRepo.findById(id);

        // Return associated medical record if exists, else return 404
        if (appointment.isPresent() && appointment.get().getMedicalRecord() != null) {
            Record medicalRecord = appointment.get().getMedicalRecord();
            return ResponseEntity.ok(medicalRecord);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for this appointment.");
        }
    }
}
