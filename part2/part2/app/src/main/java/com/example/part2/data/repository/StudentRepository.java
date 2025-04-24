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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository for managing student-related database operations.
 * Acts as an abstraction layer between ViewModels and data sources (DAO).
 */
public class StudentRepository {

    // DAO interfaces for database operations
    private final StudentDao studentDao;
    private final CourseStudentDao courseStudentDao;
    private final CourseDao courseDao;

    // Single-thread executor for database operations (prevents UI freezing)
    private final ExecutorService executorService;

    public StudentRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.studentDao = db.studentDao();
        this.courseStudentDao = db.courseStudentDao();
        this.courseDao = db.courseDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Retrieves students enrolled in a specific course asynchronously.
     * @param courseCode The course code to filter by
     * @param callback Callback to handle success/error responses
     */
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


    /**
     * Gets a course ID by its code asynchronously.
     * @param courseCode The course code to look up
     * @param callback Callback with the course ID or error
     */
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

    /**
     * Retrieves a student by matriculation number asynchronously.
     * @param matricNumber The student's matric number
     * @param callback Callback with Student object or error
     */
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

    /**
     * Inserts a new student record asynchronously.
     * @param student The Student object to insert
     * @param callback Callback with the generated ID or error
     */
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

    /**
     * Updates an existing student record asynchronously.
     * @param student The updated Student object
     * @param callback Callback for success/error
     */
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



    /**
     * Checks if a student is enrolled in a course asynchronously.
     * @param courseId The course ID to check
     * @param studentId The student ID to check
     * @param callback Callback with boolean result or error
     */
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

    /**
     * Enrolls a student in a course asynchronously.
     * @param courseId The course ID
     * @param studentId The student ID
     * @param callback Callback for success/error
     */
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


    /**
     * Callback interface for asynchronous operations.
     * @param <T> Type of the result data
     */
    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    // ========== LiveData Methods (for UI observation) ========== //


    /**
     * Gets a student with their courses by student ID (LiveData).
     * @param studentId The student ID
     * @return LiveData containing StudentWithCourses
     */
    public LiveData<StudentWithCourses> getStudentWithCourses(int studentId) {
        return studentDao.getStudentWithCoursesById(studentId);
    }

    /**
     * Unenrolls a student from a course asynchronously.
     * @param courseId The course ID
     * @param studentId The student ID
     * @param callback Callback for success/error
     */
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

    /**
     * Gets a student by ID (LiveData).
     * @param studentId The student ID
     * @return LiveData containing Student
     */
    public LiveData<Student> getStudentById(int studentId) {
        return studentDao.getStudentById(studentId);
    }
}