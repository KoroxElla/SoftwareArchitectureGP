package com.example.part2.data.dao;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.part2.data.entities.Course;
import com.example.part2.data.entities.CourseStudentCrossRef;
import com.example.part2.data.entities.Student;

@Database(entities = {Course.class, Student.class, CourseStudentCrossRef.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CourseDao courseDao();
    public abstract StudentDao studentDao();
    public abstract CourseStudentDao courseStudentDao();
}
