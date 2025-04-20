package com.example.part2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.part2.R;
import com.example.part2.data.entities.Course;
import com.example.part2.viewmodel.CourseViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setTitle("Home Page");
        // Initialize ViewModel
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // Log all courses
        setupCourseLogging();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void setupCourseLogging() {
        courseViewModel.getAllCourses().observe(this, courses -> {
            if (courses != null) {
                Log.d("MainActivity", "Total courses: " + courses.size());

                for (Course course : courses) {
                    Log.d("MainActivity", "Course: " + course.getCourseCode() +
                            " | Name: " + course.getCourseName() +
                            " | Lecturer: " + course.getLecturerName());
                }
            } else {
                Log.d("MainActivity", "No courses found");
            }
        });
    }
    public void createCourse(View v){
        Intent i = new Intent(this, CreateCourseActivity.class);
        startActivity(i);
    }
}