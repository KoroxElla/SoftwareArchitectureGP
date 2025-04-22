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
    private EditText studentName, studentEmail;
    private Button btnAdd;
    private int courseId;
    private StudentViewModel studentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        setTitle("Add Student");

        courseId = getIntent().getIntExtra("courseId", -1);
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        studentName = findViewById(R.id.studentName);
        studentEmail = findViewById(R.id.studentEmail);
        btnAdd = findViewById(R.id.btnAddStudent);

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

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and email are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pass empty string for username - it will be generated from email
        studentViewModel.addStudentToCourse(name, email, "", courseId);
    }
}