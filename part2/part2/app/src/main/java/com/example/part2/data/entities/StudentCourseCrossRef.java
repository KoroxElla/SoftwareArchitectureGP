package com.example.part2.data.entities;

import androidx.room.Entity;
import androidx.room.Index;

/**
 * Junction table for the many-to-many relationship between Students and Courses.
 * Uses composite primary key (userName + courseCode) for efficient lookups.
 */
@Entity(
        primaryKeys = {"userName", "courseCode"},  // Composite primary key
        indices = {
                @Index("userName"),     // Faster queries by userName
                @Index("courseCode")   // Faster queries by courseCode
        }
)
public class StudentCourseCrossRef {
    public String userName;    // Part of composite key (references Student)
    public String courseCode;  // Part of composite key (references Course)
}