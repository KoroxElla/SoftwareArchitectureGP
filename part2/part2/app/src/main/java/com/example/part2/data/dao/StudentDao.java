package com.example.part2.data.dao;

import androidx.lifecycle.LiveData;
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

    // Insert and return studentId
    @Insert
    long insertStudent(Student student);

    // Used in EditStudentActivity (updates student without returning ID)
    @Insert
    void insertStudentVoid(Student student);

    // Get all students with their courses (not required unless needed)
    @Transaction
    @Query("SELECT * FROM Student")
    List<StudentWithCourses> getStudentsWithCourses();


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
    // ✅ Task 6 - Get one student and their enrolled courses (LiveData)
    @Transaction
    @Query("SELECT * FROM Student WHERE userName = :userName")
    LiveData<StudentWithCourses> getStudentWithCourses(String userName);

    // ✅ Task 7 - Get student by username (LiveData for observing in UI)
    @Query("SELECT * FROM Student WHERE userName = :userName LIMIT 1")
    LiveData<Student> getStudentByUsername(String userName);

    // ✅ Task 7 - Get student by username (Sync version for threads)
    @Query("SELECT * FROM Student WHERE userName = :userName LIMIT 1")
    Student getStudentByUsernameSync(String userName);

}
