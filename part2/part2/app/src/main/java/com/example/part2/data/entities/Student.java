package com.example.part2.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a Student entity in the database.
 * Auto-generates studentId as primary key.
 */
@Entity
public class Student {
    @PrimaryKey(autoGenerate = true)  // Auto-incrementing ID
    private int studentId;

    private String matricNumber;  // Unique student identifier (e.g., "123456")
    private String name;          // Full name
    private String email;         // Contact email
    private String userName;      // Derived from email (e.g., "john@domain.com" → "john")

    // ========== Getters & Setters ========== //
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getMatricNumber() { return matricNumber; }
    public void setMatricNumber(String matricNumber) {
        this.matricNumber = matricNumber;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email;
        // Could derive userName here: this.userName = email.split("@")[0];
    }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
