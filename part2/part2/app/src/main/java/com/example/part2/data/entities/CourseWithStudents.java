package com.example.part2.data.entities;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class CourseWithStudents {
    @Embedded
    public Course course;

    @Relation(
            parentColumn = "courseId",
            entityColumn = "studentId",
            associateBy = @Junction(CourseStudentCrossRef.class)
    )
    public List<Student> students;
}

