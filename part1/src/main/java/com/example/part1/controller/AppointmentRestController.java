package com.example.part1.controller;

import com.example.part1.domain.Appointments;
import com.example.part1.domain.Doctor;
import com.example.part1.domain.Patient;
import com.example.part1.domain.MedicalRecord;
import com.example.part1.repo.AppointmentRepo;
import com.example.part1.repo.DoctorRepo;
import com.example.part1.repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/appointments")
public class AppointmentRestController {

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private PatientRepo patientRepo;

    // Endpoint #14 - Get handler returns all appointments
    @GetMapping
    public List<Appointments> getAllAppointments() {
        return appointmentRepo.findAll();
    }


    // Endpoint #15 - Post endpoint to create new appointments
    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointments appointment) {
        //Check if patient and doctor exist
        if (appointment.getPatient() == null || appointment.getDoctor() == null) {
            return ResponseEntity.badRequest().body("Doctor and Patient must be provided.");
        }
        Optional<Patient> patient = patientRepo.findById(appointment.getPatient().getId());
        Optional<Doctor> doctor = doctorRepo.findById(appointment.getDoctor().getId());

        if (patient.isEmpty() || doctor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient or doctor not found.");
        }
        if (appointment.getAppointmentDate() == null) {
            return ResponseEntity.badRequest().body("Appointment date must be provided.");
        }
        if (appointment.getStatus() == null) {
            return ResponseEntity.badRequest().body("Appointment status must be provided.");
        }
        if (appointment.getNotes() == null) {
            return ResponseEntity.badRequest().body("Notes must be provided.");
        }
        appointment.setPatient(patient.get());
        appointment.setDoctor(doctor.get());
        //save appointment
        Appointments saved = appointmentRepo.save(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Endpoint #16 - Handles requests to  get appointments by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
            Optional<Appointments> optionalAppointment = appointmentRepo.findById(id);
            if (optionalAppointment.isPresent()) {
                return ResponseEntity.ok(optionalAppointment.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
            }
    }

    //Endpoint #17 - Handles appointment updates by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody Appointments updated) {
        Optional<Appointments> optionalAppointment = appointmentRepo.findById(id);
        if (optionalAppointment.isPresent()) {
            Appointments existing = optionalAppointment.get();
            existing.setAppointmentDate(updated.getAppointmentDate());
            existing.setStatus(updated.getStatus());
            existing.setNotes(updated.getNotes());

            //Save and update appointments
            Appointments saved = appointmentRepo.save(existing);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
        }
    }

    // Endpoint #18 - Handle delete requests by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        Optional<Appointments> optionalAppointment = appointmentRepo.findById(id);
        if (optionalAppointment.isPresent()) {
            appointmentRepo.delete(optionalAppointment.get());
            return ResponseEntity.ok("Appointment deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
        }
    }

    //Endpoint #19 - handle GET requests to fetch records associated with an appointment
    @GetMapping("/{id}/medical-record")
    public ResponseEntity<?> getAppointmentRecord(@PathVariable Long id) {
        Optional<Appointments> appointment = appointmentRepo.findById(id);

        //If an appointment exists and has record
        if (appointment.isPresent() && appointment.get().getRecord() != null) {
            MedicalRecord record = appointment.get().getRecord();
            return ResponseEntity.ok(record);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("MedicalRecord not found for this appointment.");
        }
    }
}
