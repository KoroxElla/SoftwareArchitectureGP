package com.example.part1.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Represents a Doctor in the healthcare system.
 * A doctor can have multiple appointments with one or more patients.
 */
@Entity // Marks this class as a JPA entity (maps to a database table)
public class Doctor {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID
    private Long id; // Unique identifier for the doctor

    @NotBlank(message = "Name is required")
    private String name; // Full name of the doctor
    private String specialisation; // Medical specialisation (e.g., Cardiologist)
    private String email; // Email address of the doctor
    private String phoneNumber; // Contact number of the doctor

    /**
     * One-to-Many relationship with Appointments.
     * - A doctor can have multiple appointments.
     * - `mappedBy = "doctor"` indicates that the `Appointments` entity owns the relationship.
     * - `cascade = CascadeType.ALL` ensures operations cascade to appointments.
     * - `orphanRemoval = true` removes appointments if unlinked from this doctor.
     * - `@JsonManagedReference` prevents JSON infinite recursion (forward part of the reference).
     */
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("doctor-appointments")
    private List<Appointments> appointments;

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

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
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

    public List<Appointments> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointments> appointments) {
        this.appointments = appointments;
    }
}
