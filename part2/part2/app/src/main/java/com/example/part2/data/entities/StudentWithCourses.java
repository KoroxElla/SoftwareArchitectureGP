package com.example.part2.data.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

/**
 * Represents a Student entity with their enrolled Courses.
 * Room will automatically fetch the related courses using the junction table.
 */
public class StudentWithCourses {
    @Embedded  // Includes all fields from the Student entity
    public Student student;

    /**
     * Defines the many-to-many relationship between Students and Courses.
     * Uses CourseStudentCrossRef as the junction table.
     */
    @Relation(
            parentColumn = "studentId",  // From Student entity
            entityColumn = "courseId",   // From Course entity
            associateBy = @Junction(CourseStudentCrossRef.class))  // Junction table
    public List<Course> courses;
}