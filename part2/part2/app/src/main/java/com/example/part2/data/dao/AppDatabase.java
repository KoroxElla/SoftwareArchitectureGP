package com.example.part2.data.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.part2.data.entities.Course;
import com.example.part2.data.entities.Student;
import com.example.part2.data.entities.CourseStudentCrossRef;

@Database(
        entities = {
                Course.class,
                Student.class,
                CourseStudentCrossRef.class  //  Using the correct cross-ref
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "app_database"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

    //  Abstract DAO interfaces
    public abstract CourseDao courseDao();
    public abstract StudentDao studentDao();
    public abstract CourseStudentDao courseStudentDao();
}