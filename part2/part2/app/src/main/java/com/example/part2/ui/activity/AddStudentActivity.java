package com.example.part2.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.part2.R;
import com.example.part2.data.entities.Student;
import com.example.part2.data.repository.StudentRepository;

public class AddStudentActivity extends AppCompatActivity {
    private EditText studentName;
    private EditText studentEmail;
    private EditText studentUsername;
    private int courseId;
    private StudentRepository studentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        studentName = findViewById(R.id.studentName);
        studentEmail = findViewById(R.id.studentEmail);
        studentUsername = findViewById(R.id.studentUsername);
        Button btnAdd = findViewById(R.id.btnAddStudent);

        courseId = getIntent().getIntExtra("courseId", -1);
        studentRepository = new StudentRepository(getApplication());

        btnAdd.setOnClickListener(v -> addStudent());
    }

    private void addStudent() {
        String name = studentName.getText().toString().trim();
        String email = studentEmail.getText().toString().trim();
        String username = studentUsername.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Student existing = studentRepository.getStudentByUsernameSync(username);
            int studentId;

            if (existing != null) {
                studentId = existing.getStudentId();

                if (studentRepository.isStudentEnrolled(courseId, studentId)) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Student is already enrolled", Toast.LENGTH_SHORT).show());
                    return;
                }
            } else {
                Student student = new Student(name, email, username);
                studentId = (int) studentRepository.insertAndGetId(student);
            }

            studentRepository.enrollStudent(courseId, studentId);

            runOnUiThread(() -> {
                Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show();
                finish(); // return to CourseDetails
            });
        }).start();
    }
}