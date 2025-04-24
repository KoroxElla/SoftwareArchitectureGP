package com.example.part1.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.sql.Timestamp;

/**
 * Represents an Appointment between a Patient and a Doctor.
 * Acts as a join table in a many-to-many relationship.
 */
@Entity // Marks this class as a JPA entity (maps to a database table)
public class Appointments {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID
    private Long id; // Unique identifier for the appointment

    private Timestamp appointmentDate; // Date and time of the appointment
    private String status; // Status (e.g., "Scheduled", "Completed", "Cancelled")
    private String notes; // Additional notes about the appointment

    /**
     * Many-to-One relationship with Patient.
     * - Many appointments can belong to one patient.
     * - `@JoinColumn` specifies the foreign key column in the database.
     * - `@JsonBackReference` prevents JSON infinite recursion (backward part of the reference).
     */
    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonBackReference("patient-appointments")
    private Patient patient;

    /**
     * Many-to-One relationship with Doctor.
     * - Many appointments can belong to one doctor.
     * - `@JoinColumn` specifies the foreign key column in the database.
     * - `@JsonBackReference` prevents JSON infinite recursion (backward part of the reference).
     */
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @JsonBackReference("doctor-appointments")
    private Doctor doctor;

    /**
     * One-to-One relationship with MedicalRecord.
     * - Each appointment can have one medical record.
     * - `mappedBy = "appointment"` indicates that `MedicalRecord` owns the relationship.
     * - `cascade = CascadeType.ALL` ensures operations cascade to the medical record.
     * - `orphanRemoval = true` removes the medical record if unlinked from this appointment.
     */
    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference("appointment-medical-record")
    private MedicalRecord medicalRecord;

    // ========== Getters & Setters ========== //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Timestamp appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }
}
