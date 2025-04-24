package com.example.part2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.part2.R;
import com.example.part2.data.entities.Course;
import com.example.part2.ui.adapters.CourseAdapter;
import com.example.part2.viewmodel.CourseViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CourseAdapter.OnCourseClickListener {
    private CourseViewModel courseViewModel; // ViewModel for course data
    private CourseAdapter adapter; // Adapter for RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge display
        setContentView(R.layout.activity_main);
        setTitle("Home Page");

        // Initialize RecyclerView
        // RecyclerView for course list
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with click listener
        adapter = new CourseAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Initialize ViewModel
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // Observe course list changes
        courseViewModel.getAllCourses().observe(this, courses -> {
            if (courses != null) {
                Log.d("MainActivity", "Total courses: " + courses.size());
                adapter.setCourseList(courses); // Update adapter data
            } else {
                Log.d("MainActivity", "No courses found");
            }
        });

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Provides haptic feedback when long-pressing a course
     */
    private void performHapticFeedback() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    /**
     * Shows confirmation dialog before deleting a course
     */
    private void showDeleteConfirmation(Course course) {
        new AlertDialog.Builder(this)
                .setTitle("Delete " + course.getCourseName() + "?")
                .setMessage("This will permanently remove the course")
                .setPositiveButton("Delete", (d, w) -> deleteCourseWithUndo(course))
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Deletes a course with undo Snackbar functionality
     */
    private void deleteCourseWithUndo(Course course) {
        Course deletedCourse = new Course(course.getCourseCode(), course.getCourseName(), course.getLecturerName());
        courseViewModel.delete(course);

        Snackbar.make(
                findViewById(R.id.main),
                course.getCourseName() + " deleted",
                Snackbar.LENGTH_LONG
        ).setAction("UNDO", v -> courseViewModel.addCourse(deletedCourse)).show();
    }

    // === Click Listeners ===
    @Override
    public void onCourseClick(Course course) {
        // Navigate to CourseDetailsActivity
        Intent intent = new Intent(this, CourseDetailsActivity.class);
        intent.putExtra("courseCode", course.getCourseCode());
        intent.putExtra("courseName", course.getCourseName());
        intent.putExtra("lecturer", course.getLecturerName());
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onCourseLongClick(Course course) {
        performHapticFeedback();
        showDeleteConfirmation(course);
    }

    /**
     * Handles FAB click to create new course
     */
    public void createCourse(View v) {
        startActivity(new Intent(this, CreateCourseActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}