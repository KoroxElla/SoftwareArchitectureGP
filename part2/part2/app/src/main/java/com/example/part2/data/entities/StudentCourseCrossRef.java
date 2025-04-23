package com.example.part2.data.entities;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(
        primaryKeys = {"userName", "courseCode"},
        indices = {
                @Index("userName"),
                @Index("courseCode")
        }
)
public class StudentCourseCrossRef {
    public String userName;
    public String courseCode;
}