package com.example.part2.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.part2.data.entities.Course;
import com.example.part2.data.entities.CourseWithStudents;
import com.example.part2.data.entities.Student;

import java.util.List;

@Dao
public interface CourseDao {

    // Insert a new course; abort if duplicate
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertCourse(Course course);

    // Get all courses with students enrolled (not LiveData because Room loads once)
    @Transaction
    @Query("SELECT * FROM Course")
    List<CourseWithStudents> getCoursesWithStudents();

    // Delete a course
    @Delete
    void deleteCourse(Course course);

    // Get all courses (live)
    @Query("SELECT * FROM Course")
    LiveData<List<Course>> getAllCoursesLive();

    // Get a course by its code (live)
    @Query("SELECT * FROM Course WHERE courseCode = :code LIMIT 1")
    LiveData<Course> getCourseByCode(String code);

    // Synchronous version for internal use (like in ViewModel/Repo)
    @Query("SELECT * FROM Course WHERE courseCode = :code LIMIT 1")
    Course getCourseByCodeSync(String code);

    // Get all students enrolled in a course by courseId
    @Query("SELECT s.* FROM Student s INNER JOIN CourseStudentCrossRef sc ON s.studentId = sc.studentId WHERE sc.courseId = :courseId")
    LiveData<List<Student>> getStudentsInCourse(int courseId);
}