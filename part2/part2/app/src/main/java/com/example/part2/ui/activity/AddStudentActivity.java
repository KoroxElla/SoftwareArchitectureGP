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
    private EditText studentName, studentEmail, studentUsername;
    private Button btnAdd;
    private int courseId;
    private StudentRepository studentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        setTitle("Add Student");
        //Get courseId from intent
        courseId = getIntent().getIntExtra("courseId", -1);
        studentRepository = new StudentRepository(getApplication());
        studentName = findViewById(R.id.studentName);
        studentEmail = findViewById(R.id.studentEmail);
        studentUsername = findViewById(R.id.studentUsername);
        btnAdd = findViewById(R.id.btnAddStudent);
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
            //Get existing student by username
            Student existing = studentRepository.getStudentByUsername(username);
            if (existing != null && studentRepository.isStudentEnrolled(courseId, existing.getStudentId())) {
                runOnUiThread(() -> Toast.makeText(this, "Student already enrolled", Toast.LENGTH_SHORT).show());
            } else {
                int studentId;
                if (existing == null) {
                    //Create new student
                    Student newStudent = new Student();
                    newStudent.setName(name);
                    newStudent.setEmail(email);
                    newStudent.setUserName(username);
                    studentId = (int) studentRepository.insertAndGetId(newStudent);
                } else {
                    //Use existing studentId
                    studentId = existing.getStudentId();
                }
                //Enroll student in course
                studentRepository.enrollStudent(courseId, studentId);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show();
                    finish(); //Return to CourseDetails
                });
            }
        }).start();
    }
}