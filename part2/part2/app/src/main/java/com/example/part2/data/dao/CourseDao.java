package com.example.part2.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.part2.data.entities.Course;


import java.util.List;

/**
 * DAO for Course-related database operations.
 * Provides CRUD functionality for Course entities.
 */
@Dao
public interface CourseDao {

    // ========== INSERT OPERATIONS ========== //

    /**
     * Inserts a new course into the database.
     * @param course The Course object to insert
     */
    @Insert
    void insertCourse(Course course);

    // ========== DELETE OPERATIONS ========== //

    /**
     * Deletes a course from the database.
     * @param course The Course object to delete
     */
    @Delete
    void deleteCourse(Course course);

    // ========== QUERY OPERATIONS (LiveData) ========== //

    /**
     * Gets all courses with LiveData observation.
     * @return LiveData<List<Course>> that updates automatically
     */
    @Query("SELECT * FROM Course")
    LiveData<List<Course>> getAllCoursesLive();

    /**
     * Gets a course by its unique code (LiveData).
     * @param code The course code (e.g., "CS101")
     * @return LiveData<Course> that can be observed for changes
     */
    @Query("SELECT * FROM Course WHERE courseCode = :code LIMIT 1")
    LiveData<Course> getCourseByCode(String code);

    // ========== QUERY OPERATIONS (Synchronous) ========== //

    /**
     * Gets a course ID by its code (blocking call).
     * @param courseCode The course code to look up
     * @return The courseId or 0 if not found
     */
    @Query("SELECT courseId FROM Course WHERE courseCode = :courseCode LIMIT 1")
    int getCourseIdByCode(String courseCode);
}
