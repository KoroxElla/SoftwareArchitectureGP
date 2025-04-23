package com.example.part1.domain;

import jakarta.persistence.*;
import java.sql.Timestamp;

/**
 * Represents a Medical Record in the healthcare system.
 * Each medical record is linked to a single appointment and contains
 * diagnosis, treatment, and additional notes.
 */
@Entity // Marks this class as a JPA entity (maps to a database table)
public class MedicalRecord {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID
    private Long id; // Unique identifier for the medical record

    private Timestamp recordDate; // Date and time when the record was created
    private String diagnosis; // Medical diagnosis for the patient
    private String treatment; // Prescribed treatment or medication
    private String notes; // Additional notes or observations

    /**
     * One-to-One relationship with Appointments.
     * - Each medical record is associated with exactly one appointment.
     * - `@JoinColumn` specifies the foreign key column (`appointment_id`) in the database.
     * - `unique = true` ensures that each appointment can have only one medical record.
     * - The owning side of the relationship is `Appointments` (as it has `mappedBy`).
     */
    @OneToOne
    @JoinColumn(name = "appointment_id", unique = true)
    private Appointments appointment;

    // ========== Getters & Setters ========== //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Timestamp recordDate) {
        this.recordDate = recordDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Appointments getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointments appointment) {
        this.appointment = appointment;
    }
}