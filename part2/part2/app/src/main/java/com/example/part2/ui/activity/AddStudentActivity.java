package com.example.part2.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.part2.R;
import com.example.part2.data.entities.Student;
import com.example.part2.data.repository.StudentRepository;
import com.example.part2.viewmodel.StudentViewModel;

public class AddStudentActivity extends AppCompatActivity {
    private EditText studentName, studentEmail, studentUsername;
    private int courseId;
    private StudentViewModel studentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        studentName = findViewById(R.id.studentName);
        studentEmail = findViewById(R.id.studentEmail);
        studentUsername = findViewById(R.id.studentUsername);
        Button btnAdd = findViewById(R.id.btnAddStudent);

        courseId = getIntent().getIntExtra("courseId", -1);
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        setupObservers();

        btnAdd.setOnClickListener(v -> {
            String name = studentName.getText().toString().trim();
            String email = studentEmail.getText().toString().trim();
            String username = studentUsername.getText().toString().trim();
            studentViewModel.addStudentToCourse(name, email, username, courseId);
        });
    }

    private void setupObservers() {
        studentViewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        studentViewModel.getStudentAdded().observe(this, added -> {
            if (added != null && added) {
                Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
