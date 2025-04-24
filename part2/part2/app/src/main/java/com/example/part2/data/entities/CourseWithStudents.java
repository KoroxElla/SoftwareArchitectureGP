package com.example.part2.data.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

/**
 * Represents a Course entity with its enrolled Students.
 * Room will automatically fetch the related students using the junction table.
 */
public class CourseWithStudents {
    @Embedded  // Includes all fields from the Course entity
    public Course course;

    /**
     * Defines the many-to-many relationship between Courses and Students.
     * Uses CourseStudentCrossRef as the junction table.
     */
    @Relation(
            parentColumn = "courseId",   // From Course entity
            entityColumn = "studentId",  // From Student entity
            associateBy = @Junction(CourseStudentCrossRef.class)) // Junction table
    public List<Student> students;
}