package com.example.part2.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.part2.data.dao.AppDatabase;
import com.example.part2.data.dao.CourseDao;
import com.example.part2.data.dao.CourseStudentDao;
import com.example.part2.data.dao.StudentDao;
import com.example.part2.data.entities.CourseStudentCrossRef;
import com.example.part2.data.entities.Student;
import com.example.part2.data.entities.StudentWithCourses;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentRepository {

    private final StudentDao studentDao;
    private final CourseStudentDao courseStudentDao;
    private final CourseDao courseDao;
    private final ExecutorService executorService;

    public StudentRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        studentDao = db.studentDao();
        courseStudentDao = db.courseStudentDao();
        courseDao = db.courseDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getStudentsForCourse(String courseCode, RepositoryCallback<List<Student>> callback) {
        executorService.execute(() -> {
            try {
                int courseId = courseDao.getCourseIdByCode(courseCode);
                if (courseId <= 0) {
                    callback.onError(new Exception("Course not found"));
                    return;
                }
                List<Student> students = courseStudentDao.getStudentsForCourse(courseId);
                callback.onSuccess(students);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    // ✅ Sync method (used in AddStudentActivity or background logic)
    public Student getStudentByUsernameSync(String username) {
        try {
            return executorService.submit(() ->
                    studentDao.getStudentByUsernameSync(username)
            ).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace(); // Consider using Log.e(TAG, "error", e) in production
            return null;
        }
    }

    public void getCourseId(String courseCode, RepositoryCallback<Integer> callback) {
        executorService.execute(() -> {
            try {
                int courseId = courseDao.getCourseIdByCode(courseCode);
                callback.onSuccess(courseId);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getStudentByMatric(String matricNumber, RepositoryCallback<Student> callback) {
        executorService.execute(() -> {
            try {
                Student student = studentDao.getStudentByMatric(matricNumber);
                callback.onSuccess(student);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void insertStudent(Student student, RepositoryCallback<Long> callback) {
        executorService.execute(() -> {
            try {
                long id = studentDao.insertStudent(student);
                callback.onSuccess(id);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void updateStudent(Student student, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                studentDao.updateStudent(student);
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }
    // Insert student and return the generated ID
    public long insertAndGetId(Student student) {
        try {
            return executorService.submit(() ->
                    studentDao.insertStudent(student)
            ).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public void deleteStudent(Student student) {
        executorService.execute(() ->
                studentDao.deleteStudent(student)
        );
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
    public void removeStudentFromCourse(String courseCode, String studentMatric) {
        executorService.execute(() -> {
            int courseId = courseDao.getCourseIdByCode(courseCode);
            Student student = studentDao.getStudentByMatric(studentMatric);
            if (courseId > 0 && student != null) {
                courseStudentDao.removeStudentFromCourse(courseId, student.getStudentId());
            }
        });
    }
    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
    // Add these LiveData methods:
    public LiveData<StudentWithCourses> getStudentWithCourses(String userName) {
        return studentDao.getStudentWithCoursesByUserName(userName);
    }

    // LiveData version
    public LiveData<Student> getStudentByUsernameLive(String userName) {
        return studentDao.getStudentByUsernameLive(userName);
    }

    public LiveData<StudentWithCourses> getStudentWithCourses(int studentId) {
        return studentDao.getStudentWithCoursesById(studentId);
    }

    public void unenrollStudent(int courseId, int studentId, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                courseStudentDao.unenrollStudent(courseId, studentId);
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public LiveData<Student> getStudentById(int studentId) {
        return studentDao.getStudentById(studentId);
    }
}
