package com.example.part2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.part2.data.entities.Student;
import com.example.part2.data.entities.StudentWithCourses;
import com.example.part2.data.repository.StudentRepository;

public class StudentViewModel extends AndroidViewModel {

    private final StudentRepository repository;

    public StudentViewModel(@NonNull Application application) {
        super(application);
        repository = new StudentRepository(application);
    }

    // For Task 6 - Show student with enrolled courses
    public LiveData<StudentWithCourses> getStudentWithCourses(String userName) {
        return repository.getStudentWithCourses(userName);
    }

    // For EditStudentActivity - get basic Student by username
    public LiveData<Student> getStudentByUsername(String userName) {
        return repository.getStudentByUsername(userName);
    }

    // For EditStudentActivity - update student record
    public void updateStudent(Student student) {
        repository.updateStudent(student);
    }
}