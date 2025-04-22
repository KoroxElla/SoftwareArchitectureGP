package com.example.part2.data.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;


import com.example.part2.data.entities.Student;
import com.example.part2.data.entities.StudentWithCourses;

import java.util.List;

@Dao
public interface StudentDao {
    @Insert
    long insertStudent(Student student);

    @Transaction
    @Query("SELECT * FROM Student")
    List<StudentWithCourses> getStudentsWithCourses();

    @Query("SELECT * FROM Student WHERE userName = :username LIMIT 1")
    Student getStudentByUsername(String username);

    @Query("SELECT * FROM Student WHERE matricNumber = :matricNumber LIMIT 1")
    Student getStudentByMatric(String matricNumber);

    @Delete
    void deleteStudent(Student student);

    @Update
    void updateStudent(Student student);

    @Insert
    long insertStudentAndGetId(Student student); // Add this for getting the ID

    @Query("SELECT * FROM Student WHERE studentId = :studentId")
    Student getStudentById(int studentId);
}
