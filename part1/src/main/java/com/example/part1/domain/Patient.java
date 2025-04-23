package com.example.part1.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

/**
 * Represents a Patient in the healthcare system.
 * A patient can have multiple appointments with one or more doctors.
 */
@Entity // Marks this class as a JPA entity (maps to a database table)
public class Patient {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID
    private Long id; // Unique identifier for the patient

    private String name; // Full name of the patient
    private String email; // Email address of the patient
    private String phoneNumber; // Contact number of the patient
    private String address; // Physical address of the patient

    /**
     * One-to-Many relationship with Appointments.
     * - A patient can have multiple appointments.
     * - `mappedBy = "patient"` indicates that the `Appointments` entity owns the relationship.
     * - `cascade = CascadeType.ALL` ensures that operations (save, delete, etc.) cascade to appointments.
     * - `orphanRemoval = true` removes appointments if they are no longer linked to this patient.
     * - `@JsonManagedReference` prevents JSON infinite recursion (forward part of the reference).
     */
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("patient-appointments")
    private List<Appointments> appointments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<MedicalRecord> medicalRecords;


    // ========== Getters & Setters ========== //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Appointments> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointments> appointments) {
        this.appointments = appointments;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }
}