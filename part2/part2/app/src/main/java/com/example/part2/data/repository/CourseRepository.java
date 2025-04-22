package com.example.part2.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.part2.data.dao.AppDatabase;
import com.example.part2.data.dao.CourseDao;
import com.example.part2.data.entities.Course;

import java.util.List;
import java.util.concurrent.ExecutionException;
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

    // For LiveData observation (UI layer)
    public LiveData<Boolean> isCourseCodeUnique(String courseCode) {
        MediatorLiveData<Boolean> result = new MediatorLiveData<>();
        LiveData<Course> source = courseDao.getCourseByCode(courseCode);

        result.addSource(source, course -> {
            result.setValue(course == null);
            result.removeSource(source);
        });

        return result;
    }

    // For synchronous checks (if needed)
    public boolean isCourseCodeUniqueSync(String courseCode) throws ExecutionException, InterruptedException {
        return executorService.submit(() ->
                courseDao.getCourseByCodeSync(courseCode) == null
        ).get(); // Note: This blocks, use carefully
    }

    public void insertCourse(Course course) {
        executorService.execute(() -> courseDao.insertCourse(course));
    }

    public void deleteCourse(Course course){
        executorService.execute(() ->courseDao.deleteCourse(course));
    }

    public LiveData<List<Course>> getAllCoursesLive() {
        return courseDao.getAllCoursesLive();
    }
}

