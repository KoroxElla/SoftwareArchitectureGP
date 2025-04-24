package com.example.part2.data.entities;

import androidx.room.Entity;
import androidx.room.Index;

/**
 * Junction table for the many-to-many relationship between Courses and Students.
 * Uses composite primary key (courseId + studentId).
 */
@Entity(primaryKeys = {"courseId", "studentId"})
public class CourseStudentCrossRef {
    public int courseId;   // Part of composite key (references Course.courseId)
    public int studentId;  // Part of composite key (references Student.studentId)

    public CourseStudentCrossRef(int courseId, int studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
    }
}

