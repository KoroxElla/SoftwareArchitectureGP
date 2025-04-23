package com.example.part2.data.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.part2.data.entities.Course;
import com.example.part2.data.entities.Student;
import com.example.part2.data.entities.CourseStudentCrossRef;

/**
 * Room Database class that serves as the main access point to the persisted data.
 * Uses singleton pattern to ensure only one database instance exists.
 */
@Database(
        entities = {
                Course.class,
                Student.class,
                CourseStudentCrossRef.class  // Junction table for M:N relationship
        },
        version = 1,  // Database version (increment when schema changes)
        exportSchema = false  // Disable schema export (set to true for production migrations)
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    /**
     * Singleton pattern to get the database instance.
     * @param context Application context
     * @return The single AppDatabase instance
     */
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {  // Thread-safe initialization
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "app_database"  // Database file name
                            )
                            .fallbackToDestructiveMigration()  // Wipes DB on version mismatch
                            .build();
                }
            }
        }
        return instance;
    }

    // ========== DAO ACCESS METHODS ========== //

    /**
     * @return CourseDao instance for course-related operations
     */
    public abstract CourseDao courseDao();

    /**
     * @return StudentDao instance for student-related operations
     */
    public abstract StudentDao studentDao();

    /**
     * @return CourseStudentDao instance for enrollment operations
     */
    public abstract CourseStudentDao courseStudentDao();
}