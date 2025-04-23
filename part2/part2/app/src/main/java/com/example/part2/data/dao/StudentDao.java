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

    // Insert operations
    @Insert
    long insertStudent(Student student);  // Returns the new studentId

    // Delete operation
    @Delete
    void deleteStudent(Student student);

    // Update operation
    @Update
    void updateStudent(Student student);

    // Single student queries (LiveData)
    @Query("SELECT * FROM Student WHERE studentId = :studentId")
    LiveData<Student> getStudentById(int studentId);

    @Query("SELECT * FROM Student WHERE userName = :userName LIMIT 1")
    LiveData<Student> getStudentByUsernameLive(String userName);

    // Single student queries (sync)
    @Query("SELECT * FROM Student WHERE matricNumber = :matricNumber LIMIT 1")
    Student getStudentByMatric(String matricNumber);

    @Query("SELECT * FROM Student WHERE userName = :userName LIMIT 1")
    Student getStudentByUsernameSync(String userName);

    // Student with courses relationships (LiveData)
    @Transaction
    @Query("SELECT * FROM Student WHERE userName = :userName")
    LiveData<StudentWithCourses> getStudentWithCoursesByUserName(String userName);

    @Transaction
    @Query("SELECT * FROM Student WHERE studentId = :studentId")
    LiveData<StudentWithCourses> getStudentWithCoursesById(int studentId);

    // All students queries
    @Transaction
    @Query("SELECT * FROM Student")
    LiveData<List<StudentWithCourses>> getAllStudentsWithCourses();

    @Transaction
    @Query("SELECT * FROM Student")
    List<StudentWithCourses> getStudentsWithCoursesSync();
}
