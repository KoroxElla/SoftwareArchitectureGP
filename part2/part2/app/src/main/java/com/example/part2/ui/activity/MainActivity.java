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

public class MainActivity extends AppCompatActivity implements CourseAdapter.OnCourseClickListener
 {

    private CourseViewModel courseViewModel;
    private CourseAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setTitle("Home Page");

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with click listener
        adapter = new CourseAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);


        // Initialize ViewModel
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // Observe courses and update adapter
        courseViewModel.getAllCourses().observe(this, courses -> {
            if (courses != null) {
                Log.d("MainActivity", "Total courses: " + courses.size());
                adapter.setCourseList(courses);
            } else {
                Log.d("MainActivity", "No courses found");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void performHapticFeedback() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    private void showDeleteConfirmation(Course course) {
        new AlertDialog.Builder(this)
                .setTitle("Delete " + course.getCourseName() + "?")
                .setMessage("This will permanently remove the course")
                .setPositiveButton("Delete", (d, w) -> deleteCourseWithUndo(course))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteCourseWithUndo(Course course) {
        // Make a copy of the course for potential undo
        Course deletedCourse = new Course(course.getCourseCode(), course.getCourseName(), course.getLecturerName());

        // Delete the original course
        courseViewModel.delete(course);

        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.main),
                course.getCourseName() + " deleted",
                Snackbar.LENGTH_LONG
        );

        snackbar.setAction("UNDO", v -> {
            // Use your actual method name (addCourse instead of insert)
            courseViewModel.addCourse(deletedCourse);
        });

        snackbar.show();
    }


    @Override
    public void onCourseClick(Course course) {
        Intent intent = new Intent(this, CourseDetailsActivity.class);
        intent.putExtra("courseCode", course.getCourseCode());
        intent.putExtra("courseName", course.getCourseName());
        intent.putExtra("lecturer", course.getLecturerName());
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

     @Override
     public void onCourseLongClick(Course course) {
         performHapticFeedback(); // Optional: vibration feedback
         showDeleteConfirmation(course); // Show the alert dialog to delete
     }

     public void createCourse(View v) {
        Intent i = new Intent(this, CreateCourseActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

     @Override
     public void onPointerCaptureChanged(boolean hasCapture) {
         super.onPointerCaptureChanged(hasCapture);
     }
 }