package com.example.part2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.part2.data.entities.Course;
import com.example.part2.data.repository.CourseRepository;

public class CourseViewModel extends AndroidViewModel {

    private final CourseRepository courseRepository;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
    }

    public void addCourse(Course course) {
        courseRepository.insertCourse(course);
    }
}
