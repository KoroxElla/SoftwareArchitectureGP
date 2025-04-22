package com.example.part2.data.repository;

import android.app.Application;
import com.example.part2.data.dao.AppDatabase;
import com.example.part2.data.dao.CourseStudentDao;
import com.example.part2.data.dao.StudentDao;
import com.example.part2.data.entities.CourseStudentCrossRef;
import com.example.part2.data.entities.Student;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentRepository {
    private final StudentDao studentDao;
    private final CourseStudentDao courseStudentDao;
    private final ExecutorService executorService;

    public StudentRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        studentDao = db.studentDao();
        courseStudentDao = db.courseStudentDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertStudent(Student student, RepositoryCallback<Long> callback) {
        executorService.execute(() -> {
            try {
                long id = studentDao.insertStudentAndGetId(student);
                callback.onSuccess(id);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getStudentByUsername(String username, RepositoryCallback<Student> callback) {
        executorService.execute(() -> {
            try {
                Student student = studentDao.getStudentByUsername(username);
                callback.onSuccess(student);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void isStudentEnrolled(int courseId, int studentId, RepositoryCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                int count = courseStudentDao.isStudentEnrolled(courseId, studentId);
                callback.onSuccess(count > 0);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void enrollStudent(int courseId, int studentId, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                courseStudentDao.enrollStudent(new CourseStudentCrossRef(courseId, studentId));
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getStudentsForCourse(int courseId, RepositoryCallback<List<Student>> callback) {
        executorService.execute(() -> {
            try {
                List<Student> students = courseStudentDao.getStudentsForCourse(courseId);
                callback.onSuccess(students);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}
