package com.example.part2.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a Course entity in the database.
 * Auto-generates courseId as primary key.
 */
@Entity
public class Course {
    @PrimaryKey(autoGenerate = true)  // Auto-incrementing ID
    private int courseId;

    private String courseCode;    // Unique identifier (e.g., "CS101")
    private String courseName;    // Full course name (e.g., "Introduction to Programming")
    private String lecturerName;  // Instructor's name

    public Course(String courseCode, String courseName, String lecturerName) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.lecturerName = lecturerName;
    }

    // ========== Getters & Setters ========== //
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getLecturerName() { return lecturerName; }
    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }
}
