package com.example.part2.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import com.example.part2.data.dao.AppDatabase;
import com.example.part2.data.dao.CourseDao;
import com.example.part2.data.entities.Course;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository for managing course-related database operations.
 * Mediates between ViewModels and Course DAO.
 */
public class CourseRepository {

    private final CourseDao courseDao;
    private final ExecutorService executorService;

    public CourseRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.courseDao = db.courseDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Checks if a course code is unique (LiveData observer pattern).
     * @param courseCode The course code to check
     * @return LiveData<Boolean> true if code is available
     */
    public LiveData<Boolean> isCourseCodeUnique(String courseCode) {
        MediatorLiveData<Boolean> result = new MediatorLiveData<>();
        LiveData<Course> source = courseDao.getCourseByCode(courseCode);

        result.addSource(source, course -> {
            result.setValue(course == null); // True if no course exists with this code
            result.removeSource(source); // One-time check
        });

        return result;
    }


    /**
     * Inserts a new course asynchronously.
     * @param course The Course object to insert
     */
    public void insertCourse(Course course) {
        executorService.execute(() -> courseDao.insertCourse(course));
    }

    /**
     * Deletes a course asynchronously.
     * @param course The Course object to delete
     */
    public void deleteCourse(Course course) {
        executorService.execute(() -> courseDao.deleteCourse(course));
    }

    /**
     * Gets all courses (LiveData for UI observation).
     * @return LiveData<List<Course>>
     */
    public LiveData<List<Course>> getAllCoursesLive() {
        return courseDao.getAllCoursesLive();
    }

}