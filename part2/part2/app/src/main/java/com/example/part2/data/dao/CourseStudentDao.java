package com.example.part2.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.part2.data.entities.CourseStudentCrossRef;

@Dao
public interface CourseStudentDao {

    // Enroll a student in a course
    @Insert
    void enrollStudent(CourseStudentCrossRef crossRef);

    //  Remove all students from a specific course
    @Query("DELETE FROM CourseStudentCrossRef WHERE courseId = :courseId")
    void removeStudentsFromCourse(int courseId);

    //  Remove a student from all their courses
    @Query("DELETE FROM CourseStudentCrossRef WHERE studentId = :studentId")
    void removeStudentFromAllCourses(int studentId);

    //  Remove a specific student from a specific course
    @Query("DELETE FROM CourseStudentCrossRef WHERE courseId = :courseId AND studentId = :studentId")
    void removeStudentFromCourse(int courseId, int studentId);

    //  Check if student is enrolled in course
    @Query("SELECT COUNT(*) FROM CourseStudentCrossRef WHERE courseId = :courseId AND studentId = :studentId")
    int isStudentEnrolled(int courseId, int studentId);
}