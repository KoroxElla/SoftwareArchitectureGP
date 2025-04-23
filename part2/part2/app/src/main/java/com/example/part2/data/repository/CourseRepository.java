package com.example.part2.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.part2.data.dao.AppDatabase;
import com.example.part2.data.dao.CourseDao;
import com.example.part2.data.dao.CourseStudentDao;
import com.example.part2.data.entities.Course;
import com.example.part2.data.entities.Student;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CourseRepository {

    private final CourseDao courseDao;
    private final CourseStudentDao courseStudentDao;
    private final ExecutorService executorService;

    public CourseRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        courseDao = db.courseDao();
        courseStudentDao = db.courseStudentDao();
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

    // ✅ Insert new course
    public void insertCourse(Course course) {
        executorService.execute(() -> courseDao.insertCourse(course));
    }

    public void deleteCourse(Course course) {
        executorService.execute(() -> courseDao.deleteCourse(course));
    }

    // ✅ Get all courses (LiveData)
    public LiveData<List<Course>> getAllCoursesLive() {
        return courseDao.getAllCoursesLive();
    }

    // ✅ Task 7 - Get students enrolled in a course (by courseId)
    public LiveData<List<Student>> getStudentsInCourse(int courseId) {
        return courseDao.getStudentsInCourse(courseId);
    }

    // ✅ Task 7 - Unenroll student from course
    public void unenrollStudentFromCourse(int courseId, int studentId) {
        executorService.execute(() -> courseStudentDao.removeStudentFromCourse(courseId, studentId));
    }
}