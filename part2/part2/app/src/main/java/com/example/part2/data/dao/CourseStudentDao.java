package com.example.part2.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.part2.data.entities.CourseStudentCrossRef;
import com.example.part2.data.entities.Student;

import java.util.List;

/**
 * DAO for managing the many-to-many relationship between Courses and Students.
 * Handles enrollment operations using the CourseStudentCrossRef junction table.
 */
@Dao
public interface CourseStudentDao {

    // ========== ENROLLMENT OPERATIONS ========== //

    /**
     * Enrolls a student in a course by creating a junction table record.
     * @param crossRef The relationship record containing courseId and studentId
     */
    @Insert
    void enrollStudent(CourseStudentCrossRef crossRef);

    /**
     * Checks if a student is enrolled in a specific course.
     * @param courseId The course ID to check
     * @param studentId The student ID to check
     * @return Count of matching records (0 = not enrolled)
     */
    @Query("SELECT COUNT(*) FROM COURSESTUDENTCROSSREF WHERE courseId = :courseId AND studentId = :studentId")
    int isStudentEnrolled(int courseId, int studentId);

    // ========== RELATIONSHIP QUERIES ========== //

    /**
     * Gets all students enrolled in a specific course.
     * Uses @Transaction for consistent data fetch.
     * @param courseId The course ID to filter by
     * @return List<Student> enrolled in the course
     */
    @Transaction
    @Query("SELECT * FROM Student WHERE studentId IN (SELECT studentId FROM CourseStudentCrossRef WHERE courseId = :courseId)")
    List<Student> getStudentsForCourse(int courseId);

    /**
     * Unenrolls a student from a course by deleting the junction record.
     * @param courseId The course ID
     * @param studentId The student ID
     */
    @Query("DELETE FROM CourseStudentCrossRef WHERE courseId = :courseId AND studentId = :studentId")
    void unenrollStudent(int courseId, int studentId);
}

