package com.example.part2.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.part2.data.entities.CourseStudentCrossRef;
import com.example.part2.data.entities.Student;

import java.util.List;

@Dao
public interface CourseStudentDao {

    // Enroll a student in a course
    @Insert
    void enrollStudent(CourseStudentCrossRef crossRef);

    @Query("SELECT COUNT(*) FROM COURSESTUDENTCROSSREF WHERE courseId = :courseId AND studentId = :studentId")
    int isStudentEnrolled(int courseId, int studentId);

    // Remove all students from a specific course
    @Query("DELETE FROM CourseStudentCrossRef WHERE studentId = :studentId")
    void removeStudentFromAllCourses(int studentId);

    // Remove a student from all courses
    @Query("DELETE FROM CourseStudentCrossRef WHERE courseId = :courseId AND studentId = :studentId")
    void removeStudentFromCourse(int courseId, int studentId);

    @Query("SELECT studentId FROM CourseStudentCrossRef WHERE courseId = :courseId")
    List<Integer> getStudentIdsForCourse(int courseId);

    @Transaction
    @Query("SELECT * FROM Student WHERE studentId IN (SELECT studentId FROM CourseStudentCrossRef WHERE courseId = :courseId)")
    List<Student> getStudentsForCourse(int courseId);

    @Query("DELETE FROM CourseStudentCrossRef WHERE courseId = :courseId AND studentId = :studentId")
    void unenrollStudent(int courseId, int studentId);
}

