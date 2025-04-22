package com.example.part2.data.entities;

import androidx.room.Entity;

@Entity(primaryKeys = {"courseId", "studentId"})
public class CourseStudentCrossRef {
    public int courseId;
    public int studentId;

    public CourseStudentCrossRef(int courseId, int studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
    }
}

