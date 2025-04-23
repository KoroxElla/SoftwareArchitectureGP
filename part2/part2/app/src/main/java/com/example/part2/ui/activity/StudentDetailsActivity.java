package com.example.part2.ui.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    private ActivityStudentDetailsBinding binding;
    private StudentViewModel studentViewModel;
    private int studentId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Student Details");



        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        studentId = getIntent().getIntExtra("studentId", -1);

        if (studentId != -1) {
            loadStudentDetails();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Student ID not found")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .setCancelable(false)
                    .show();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    //Load and display student details with enrolled courses
    private void loadStudentDetails() {
        studentViewModel.getStudentWithCourses(studentId).observe(this, studentWithCourses -> {
            if (studentWithCourses != null) {
                Student student = studentWithCourses.student;
                binding.studentName.setText(student.getName());
                binding.studentEmail.setText(student.getEmail());
                binding.studentMatric.setText(student.getMatricNumber());

                // Setup courses RecyclerView
                if (studentWithCourses.courses != null && !studentWithCourses.courses.isEmpty()) {
                    CourseInStudentAdapter adapter = new CourseInStudentAdapter(studentWithCourses.courses);
                    binding.coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                    binding.coursesRecyclerView.setAdapter(adapter);
                } else {
                    TextView noCourses = new TextView(this);
                    noCourses.setText("No enrolled courses");
                    noCourses.setTextSize(16);
                    noCourses.setGravity(Gravity.CENTER);
                    binding.coursesRecyclerView.setVisibility(View.GONE);
                    ((ViewGroup) binding.getRoot()).addView(noCourses);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // just close this activity and go back
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}