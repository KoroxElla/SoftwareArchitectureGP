package com.example.part2.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.part2.R;
import com.example.part2.viewmodel.StudentViewModel;
import com.example.part2.data.entities.StudentWithCourses;
import com.example.part2.ui.adapters.CourseInStudentAdapter;

public class StudentDetailsActivity extends AppCompatActivity {

    private TextView nameTextView, emailTextView, usernameTextView;
    private RecyclerView coursesRecyclerView;
    private StudentViewModel studentViewModel;
    private String studentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        // Initialize views
        nameTextView = findViewById(R.id.studentName);
        emailTextView = findViewById(R.id.studentEmail);
        usernameTextView = findViewById(R.id.studentUsername);
        coursesRecyclerView = findViewById(R.id.studentCoursesRecyclerView);

        // Setup adapter
        CourseInStudentAdapter adapter = new CourseInStudentAdapter();
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        coursesRecyclerView.setAdapter(adapter);

        // Get student userName from intent
        studentUserName = getIntent().getStringExtra("studentUserName");

        // ViewModel
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        studentViewModel.getStudentWithCourses(studentUserName).observe(this, studentWithCourses -> {
            if (studentWithCourses != null) {
                nameTextView.setText(studentWithCourses.student.getName());
                emailTextView.setText(studentWithCourses.student.getEmail());
                usernameTextView.setText(studentWithCourses.student.getUserName());

                // Set course list
                adapter.setCourseList(studentWithCourses.courses);
            }
        });
    }
}