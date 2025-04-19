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

import java.util.List;

@Dao
public interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertCourse(Course course);

    @Transaction
    @Query("SELECT * FROM Course")
    List<CourseWithStudents> getCoursesWithStudents();

    @Delete
    void deleteCourse(Course course);

    @Query("SELECT * FROM Course")
    LiveData<List<Course>> getAllCoursesLive();

    // Change this to return LiveData<Course>
    @Query("SELECT * FROM Course WHERE courseCode = :code LIMIT 1")
    LiveData<Course> getCourseByCode(String code);

    // Add this if you need synchronous version too
    @Query("SELECT * FROM Course WHERE courseCode = :code LIMIT 1")
    Course getCourseByCodeSync(String code);


}
