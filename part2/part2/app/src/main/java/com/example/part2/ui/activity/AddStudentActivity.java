package com.example.part2.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.part2.R;
import com.example.part2.viewmodel.StudentViewModel;

public class AddStudentActivity extends AppCompatActivity {
    private EditText studentName, studentEmail, studentMatric;
    private Button btnAdd;
    private String courseCode;
    private StudentViewModel studentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        courseCode = getIntent().getStringExtra("courseCode");
        if (courseCode == null || courseCode.isEmpty()) {
            Toast.makeText(this, "Invalid course", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        studentName = findViewById(R.id.studentName);
        studentEmail = findViewById(R.id.studentEmail);
        studentMatric = findViewById(R.id.studentMatric);
        btnAdd = findViewById(R.id.btnAddStudent);

        // Setup observers
        studentViewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        studentViewModel.getStudentAdded().observe(this, isAdded -> {
            if (isAdded != null && isAdded) {
                finish();
            }
        });

        btnAdd.setOnClickListener(v -> addStudent());
    }

    private void addStudent() {
        String name = studentName.getText().toString().trim();
        String email = studentEmail.getText().toString().trim();
        String matric = studentMatric.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || matric.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        studentViewModel.addStudentToCourse(name, email, matric, courseCode);;
    }
}