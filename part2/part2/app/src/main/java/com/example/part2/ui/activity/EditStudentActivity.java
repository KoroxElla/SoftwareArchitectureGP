package com.example.part2.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.part2.R;
import com.example.part2.data.entities.Student;
import com.example.part2.viewmodel.StudentViewModel;

public class EditStudentActivity extends AppCompatActivity {

    private EditText editStudentName, editStudentEmail, editStudentUsername;
    private Button btnSaveChanges;
    private StudentViewModel studentViewModel;
    private Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        editStudentName = findViewById(R.id.editStudentName);
        editStudentEmail = findViewById(R.id.editStudentEmail);
        editStudentUsername = findViewById(R.id.editStudentUsername);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        String userName = getIntent().getStringExtra("studentUserName");
        if (userName == null) {
            Toast.makeText(this, "No student selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        studentViewModel.getStudentByUsername(userName).observe(this, student -> {
            if (student != null) {
                currentStudent = student;
                editStudentName.setText(student.getName());
                editStudentEmail.setText(student.getEmail());
                editStudentUsername.setText(student.getUserName());
                editStudentUsername.setEnabled(false); // prevent editing username
            }
        });

        btnSaveChanges.setOnClickListener(v -> {
            if (currentStudent != null) {
                String name = editStudentName.getText().toString().trim();
                String email = editStudentEmail.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty()) {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                currentStudent.setName(name);
                currentStudent.setEmail(email);

                studentViewModel.updateStudent(currentStudent);

                Toast.makeText(this, "Student updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}