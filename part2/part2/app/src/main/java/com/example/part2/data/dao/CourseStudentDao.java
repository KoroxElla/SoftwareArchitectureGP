package com.example.part2.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.part2.data.entities.CourseStudentCrossRef;

@Dao
public interface CourseStudentDao {
    @Insert
    void enrollStudent(CourseStudentCrossRef crossRef);

    @Query("SELECT COUNT(*) FROM COURSESTUDENTCROSSREF WHERE courseId = :courseId AND studentId = :studentId")
    int isStudentEnrolled(int courseId, int studentId);

    @Query("DELETE FROM CourseStudentCrossRef WHERE courseId = :courseId")
    void removeStudentsFromCourse(int courseId);

    @Query("DELETE FROM CourseStudentCrossRef WHERE studentId = :studentId")
    void removeStudentFromAllCourses(int studentId);
}
