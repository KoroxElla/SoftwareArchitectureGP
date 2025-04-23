package com.example.part2.ui.activity;

import android.os.Bundle;

import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.part2.data.entities.Student;
import com.example.part2.databinding.ActivityEditStudentBinding;
import com.example.part2.viewmodel.StudentViewModel;

public class EditStudentActivity extends AppCompatActivity {
    private ActivityEditStudentBinding binding;
    private StudentViewModel studentViewModel;
    private int studentId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Edit Student Details");
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        studentId = getIntent().getIntExtra("studentId", -1);


        if (studentId != -1) {
            loadStudentData();
            setupSaveButton();
        } else {
            finish();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void loadStudentData() {
        studentViewModel.getStudentById(studentId).observe(this, student -> {
            if (student != null) {
                binding.editStudentName.setText(student.getName());
                binding.editStudentEmail.setText(student.getEmail());
                binding.editStudentUsername.setText(student.getMatricNumber());
            }
        });
    }

    private void setupSaveButton() {
        binding.btnSaveChanges.setOnClickListener(v -> {
            String name = binding.editStudentName.getText().toString();
            String email = binding.editStudentEmail.getText().toString();
            String matric = binding.editStudentUsername.getText().toString();

            if (name.isEmpty() || email.isEmpty() || matric.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            Student updatedStudent = new Student();
            updatedStudent.setStudentId(studentId);
            updatedStudent.setName(name);
            updatedStudent.setEmail(email);
            updatedStudent.setMatricNumber(matric);
            // Preserve the username
            studentViewModel.getStudentById(studentId).observe(this, originalStudent -> {
                if (originalStudent != null) {
                    updatedStudent.setUserName(originalStudent.getUserName());
                    studentViewModel.updateStudent(updatedStudent);
                    finish();
                }
            });
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