package com.example.part2.data.repository;

import android.app.Application;
import com.example.part2.data.dao.AppDatabase;
import com.example.part2.data.dao.CourseStudentDao;
import com.example.part2.data.dao.StudentDao;
import com.example.part2.data.entities.CourseStudentCrossRef;
import com.example.part2.data.entities.Student;
public class StudentRepository {

    private final StudentDao studentDao;
    private final CourseStudentDao courseStudentDao;

    public StudentRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        studentDao = db.studentDao();
        courseStudentDao = db.courseStudentDao();
    }
    public long insertAndGetId(Student student) {
        return studentDao.insertStudent(student);
    }

    public Student getStudentByUsername(String username) {
        return studentDao.getStudentByUsername(username);
    }

    public boolean isStudentEnrolled(int courseId, int studentId) {
        return courseStudentDao.isStudentEnrolled(courseId, studentId) > 0;
    }

    public void enrollStudent(int courseId, int studentId) {
        courseStudentDao.enrollStudent(new CourseStudentCrossRef(courseId, studentId));
    }

}
