package com.example.part2.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.part2.data.entities.Student;
import com.example.part2.data.entities.StudentWithCourses;

@Dao
public interface StudentDao {

    //  Insert student and return generated ID
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertStudent(Student student);

    //  Optional insert without return (used if needed)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStudentVoid(Student student);

    //  Get student with enrolled courses (Room will use your @Relation)
    @Transaction
    @Query("SELECT * FROM Student WHERE userName = :userName")
    LiveData<StudentWithCourses> getStudentWithCourses(String userName);

    //  Live observation of a single student
    @Query("SELECT * FROM Student WHERE userName = :userName")
    LiveData<Student> getStudentByUsername(String userName);

    //  Synchronous access (used in repository thread operations)
    @Query("SELECT * FROM Student WHERE userName = :userName")
    Student getStudentByUsernameSync(String userName);

    //  Delete a student
    @Delete
    void deleteStudent(Student student);
}