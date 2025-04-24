package com.example.part2.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;


import com.example.part2.data.entities.Student;
import com.example.part2.data.entities.StudentWithCourses;

/**
 * Data Access Object (DAO) for Student-related database operations.
 * Defines SQL queries that Room will implement automatically.
 */
@Dao
public interface StudentDao {

    // ========== INSERT OPERATIONS ========== //

    /**
     * Inserts a new student into the database.
     * @param student The Student object to insert
     * @return The auto-generated studentId (primary key)
     */
    @Insert
    long insertStudent(Student student);

    // ========== UPDATE OPERATIONS ========== //

    /**
     * Updates an existing student record.
     * @param student The Student object with updated values
     */
    @Update
    void updateStudent(Student student);

    // ========== QUERY OPERATIONS (LiveData) ========== //

    /**
     * Gets a student by ID with LiveData observation.
     * @param studentId The student's unique ID
     * @return LiveData<Student> that can be observed for changes
     */
    @Query("SELECT * FROM Student WHERE studentId = :studentId")
    LiveData<Student> getStudentById(int studentId);

    // ========== QUERY OPERATIONS (Synchronous) ========== //

    /**
     * Gets a student by matriculation number (blocking call).
     * @param matricNumber The student's matric number
     * @return Student object or null if not found
     */
    @Query("SELECT * FROM Student WHERE matricNumber = :matricNumber LIMIT 1")
    Student getStudentByMatric(String matricNumber);

    // ========== RELATIONSHIP QUERIES ========== //

    /**
     * Gets a student with their enrolled courses (LiveData).
     * Uses @Transaction to ensure atomic operation.
     * @param studentId The student's ID
     * @return LiveData<StudentWithCourses> containing student and course list
     */
    @Transaction
    @Query("SELECT * FROM Student WHERE studentId = :studentId")
    LiveData<StudentWithCourses> getStudentWithCoursesById(int studentId);
}