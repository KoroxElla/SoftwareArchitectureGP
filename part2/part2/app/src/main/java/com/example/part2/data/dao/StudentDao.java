package com.example.part2.data.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;


import com.example.part2.data.entities.Student;
import com.example.part2.data.entities.StudentWithCourses;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert
    void insertStudent(Student student);

    @Transaction
    @Query("SELECT * FROM Student")
    List<StudentWithCourses> getStudentsWithCourses();

    @Delete
    void deleteStudent(Student student);
}
