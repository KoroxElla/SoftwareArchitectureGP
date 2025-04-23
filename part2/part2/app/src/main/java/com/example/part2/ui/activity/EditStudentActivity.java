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
    private ActivityEditStudentBinding binding; // View binding
    private StudentViewModel studentViewModel; // ViewModel
    private int studentId; // Student ID to edit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Edit Student Details");

        // Initialize ViewModel
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        // Get student ID from intent
        studentId = getIntent().getIntExtra("studentId", -1);

        if (studentId != -1) {
            loadStudentData(); // Load existing data
            setupSaveButton(); // Configure save button
        } else {
            finish(); // Close if invalid ID
        }

        // Enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Loads student data into EditText fields
     */
    private void loadStudentData() {
        studentViewModel.getStudentById(studentId).observe(this, student -> {
            if (student != null) {
                binding.editStudentName.setText(student.getName());
                binding.editStudentEmail.setText(student.getEmail());
                binding.editStudentMatric.setText(student.getMatricNumber());
            }
        });
    }

    /**
     * Configures save button with validation
     */
    private void setupSaveButton() {
        binding.btnSaveChanges.setOnClickListener(v -> {
            // Get input values
            String name = binding.editStudentName.getText().toString();
            String email = binding.editStudentEmail.getText().toString();
            String matric = binding.editStudentMatric.getText().toString();

            // Validate fields
            if (name.isEmpty() || email.isEmpty() || matric.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create updated student object
            Student updatedStudent = new Student();
            updatedStudent.setStudentId(studentId);
            updatedStudent.setName(name);
            updatedStudent.setEmail(email);
            updatedStudent.setMatricNumber(matric);

            // Preserve username from original student
            studentViewModel.getStudentById(studentId).observe(this, originalStudent -> {
                if (originalStudent != null) {
                    updatedStudent.setUserName(originalStudent.getUserName());
                    studentViewModel.updateStudent(updatedStudent);
                    finish(); // Close activity after save
                }
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}