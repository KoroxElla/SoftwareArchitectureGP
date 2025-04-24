package com.example.part2.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.util.Patterns;

import com.example.part2.R;
import com.example.part2.viewmodel.StudentViewModel;

public class AddStudentActivity extends AppCompatActivity {
    private EditText studentName, studentEmail, studentMatric;
    private Button btnAdd;
    private String courseCode; // Current course code
    private StudentViewModel studentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        setTitle("Add Student");

        // Enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Get course code from intent
        courseCode = getIntent().getStringExtra("courseCode");
        if (courseCode == null || courseCode.isEmpty()) {
            Toast.makeText(this, "Invalid course", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        // Bind views
        studentName = findViewById(R.id.studentName);
        studentEmail = findViewById(R.id.studentEmail);
        studentMatric = findViewById(R.id.studentMatric);
        btnAdd = findViewById(R.id.btnAddStudent);

        // Setup observers
        studentViewModel.getToastMessage().observe(this, message -> {
            if (message != null && !Boolean.TRUE.equals(studentViewModel.getStudentAdded().getValue())) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                btnAdd.setEnabled(true);
            }
        });

        studentViewModel.getStudentAdded().observe(this, isAdded -> {
            if (Boolean.TRUE.equals(isAdded)) {
                studentViewModel.resetStudentAddedFlag();
                Toast.makeText(this, "Student added!", Toast.LENGTH_SHORT).show();
                finish(); // Close activity on success
            }
        });

        btnAdd.setOnClickListener(v -> addStudent());
    }

    /**
     * Validates email format
     */
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Validates matric number format (6 digits)
     */
    private boolean isValidMatric(String matric) {
        return matric.matches("\\d{6}");
    }

    /**
     * Hides the soft keyboard
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) view = getWindow().getDecorView();
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Validates inputs and adds new student
     */
    private void addStudent() {
        // Get trimmed inputs
        String name = studentName.getText().toString().trim();
        String email = studentEmail.getText().toString().trim();
        String matric = studentMatric.getText().toString().trim();

        // Clear previous errors
        studentEmail.setError(null);
        studentMatric.setError(null);

        // Validate required fields
        if (name.isEmpty() || email.isEmpty() || matric.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            studentEmail.setError("Invalid email (e.g., ana@example.com)");
            return;
        }

        // Validate matric format
        if (!isValidMatric(matric)) {
            studentMatric.setError("Must be 6 digits");
            return;
        }

        hideKeyboard();
        btnAdd.setEnabled(false); // Prevent double submission
        studentViewModel.addStudentToCourse(name, email, matric, courseCode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Handle back button
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Alternative add student handler (for XML onClick)
     */
    public void AddStudentbtn(View view) {
        addStudent();
    }
}