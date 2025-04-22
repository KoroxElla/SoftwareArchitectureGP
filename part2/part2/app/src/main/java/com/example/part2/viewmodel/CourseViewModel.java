package com.example.part2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.part2.data.entities.Course;
import com.example.part2.data.entities.Student;
import com.example.part2.data.repository.CourseRepository;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private final CourseRepository courseRepository;
    private final LiveData<List<Course>> allCourses;

    // LiveData to hold error message
    public MutableLiveData<String> courseCodeError = new MutableLiveData<>();

    public CourseViewModel(@NonNull Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
        allCourses = courseRepository.getAllCoursesLive();
    }

    // Get all courses
    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    // Validate course code before adding
    public void validateCourseCode(String courseCode) {
        courseRepository.isCourseCodeUnique(courseCode).observeForever(isUnique -> {
            if (!isUnique) {
                courseCodeError.postValue("Course code already exists");
            } else if (!courseCode.matches("^[A-Z]{2}\\d{4}$")) {
                courseCodeError.postValue("Invalid format (e.g., CS1012)");
            } else {
                courseCodeError.postValue(null);
            }
        });
    }

    // Add new course
    public void addCourse(Course course) {
        courseRepository.insertCourse(course);
    }

    // Task 7: Get all students enrolled in a course (by courseId)
    public LiveData<List<Student>> getStudentsInCourse(int courseId) {
        return courseRepository.getStudentsInCourse(courseId);
    }

    //  Task 7: Unenroll student by courseId and studentId
    public void unenrollStudentFromCourse(int courseId, int studentId) {
        courseRepository.unenrollStudentFromCourse(courseId, studentId);
    }
}