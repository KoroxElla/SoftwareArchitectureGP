package com.example.part2.data.entities;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(
        primaryKeys = {"courseId", "studentId"},
        indices = {
                @Index("studentId"),
                @Index("courseId")
        }
)
public class CourseStudentCrossRef {
    public int courseId;
    public int studentId;
}