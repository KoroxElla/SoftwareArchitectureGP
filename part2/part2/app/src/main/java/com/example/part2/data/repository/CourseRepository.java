package com.example.part2.data.repository;

import android.app.Application;

import com.example.part2.data.dao.AppDatabase;
import com.example.part2.data.dao.CourseDao;
import com.example.part2.data.entities.Course;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CourseRepository {

    private CourseDao courseDao;
    private final ExecutorService executorService;

    public CourseRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        courseDao = db.courseDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertCourse(Course course) {
        executorService.execute(() -> courseDao.insertCourse(course));
    }
}

