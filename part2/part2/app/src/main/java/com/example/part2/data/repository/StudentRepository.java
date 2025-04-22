package com.example.part2.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.part2.data.dao.AppDatabase;
import com.example.part2.data.dao.StudentDao;
import com.example.part2.data.dao.CourseStudentDao;
import com.example.part2.data.entities.CourseStudentCrossRef;
import com.example.part2.data.entities.Student;
import com.example.part2.data.entities.StudentWithCourses;

import java.util.concurrent.ExecutionException;
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

    // ✅ Task 6 - Get student and courses
    public LiveData<StudentWithCourses> getStudentWithCourses(String userName) {
        return studentDao.getStudentWithCourses(userName);
    }

    // ✅ Task 7 - For editing student data (LiveData)
    public LiveData<Student> getStudentByUsername(String userName) {
        return studentDao.getStudentByUsername(userName);
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

    // ✅ Check if student is already enrolled in course
    public boolean isStudentEnrolled(int courseId, int studentId) {
        try {
            return executorService.submit(() ->
                    courseStudentDao.isStudentEnrolled(courseId, studentId) > 0
            ).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Insert student and return the generated ID
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

    // ✅ Enroll student in a course
    public void enrollStudent(int courseId, int studentId) {
        CourseStudentCrossRef crossRef = new CourseStudentCrossRef();
        crossRef.courseId = courseId;
        crossRef.studentId = studentId;
        executorService.execute(() ->
                courseStudentDao.enrollStudent(crossRef)
        );
    }

    // ✅ Update student info
    public void updateStudent(Student student) {
        executorService.execute(() ->
                studentDao.insertStudentVoid(student)
        );
    }
}