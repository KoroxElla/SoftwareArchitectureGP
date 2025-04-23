package com.example.part2.ui.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.part2.data.entities.Student;
import com.example.part2.databinding.ActivityStudentDetailsBinding;
import com.example.part2.viewmodel.StudentViewModel;
import com.example.part2.ui.adapters.CourseInStudentAdapter;

public class StudentDetailsActivity extends AppCompatActivity {
    private ActivityStudentDetailsBinding binding; // View binding for layout
    private StudentViewModel studentViewModel; // ViewModel for student data
    private int studentId; // ID of the student being displayed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Student Details");

        // Initialize ViewModel
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        // Get student ID from intent extras
        studentId = getIntent().getIntExtra("studentId", -1);

        if (studentId != -1) {
            loadStudentDetails(); // Load data if valid ID
        } else {
            // Show error dialog if invalid ID
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Student ID not found")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .setCancelable(false)
                    .show();
        }

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Loads student details and their enrolled courses from ViewModel
     */
    private void loadStudentDetails() {
        studentViewModel.getStudentWithCourses(studentId).observe(this, studentWithCourses -> {
            if (studentWithCourses != null) {
                // Update UI with student data
                Student student = studentWithCourses.student;
                binding.studentName.setText(student.getName());
                binding.studentEmail.setText(student.getEmail());
                binding.studentMatric.setText(student.getMatricNumber());

                // Setup courses RecyclerView if courses exist
                if (studentWithCourses.courses != null && !studentWithCourses.courses.isEmpty()) {
                    CourseInStudentAdapter adapter = new CourseInStudentAdapter(studentWithCourses.courses);
                    binding.coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                    binding.coursesRecyclerView.setAdapter(adapter);
                } else {
                    // Show "No courses" message if empty
                    TextView noCourses = new TextView(this);
                    noCourses.setText("No enrolled courses");
                    noCourses.setTextSize(16);
                    noCourses.setGravity(Gravity.CENTER);
                    binding.coursesRecyclerView.setVisibility(View.GONE);
                    binding.getRoot().addView(noCourses);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button click
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}