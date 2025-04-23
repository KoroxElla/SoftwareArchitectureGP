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
    private String courseCode;
    private StudentViewModel studentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        setTitle("Add Student");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } //returns to view course details

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
            //Only show toast if student was not added
            if (message != null && !Boolean.TRUE.equals(studentViewModel.getStudentAdded().getValue())) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                btnAdd.setEnabled(true);
            }
        });

        studentViewModel.getStudentAdded().observe(this, isAdded -> {
            if (Boolean.TRUE.equals(isAdded)) {
                studentViewModel.resetStudentAddedFlag();
                studentViewModel.getToastMessage().removeObservers(this);
                Toast.makeText(this, "Student added!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnAdd.setOnClickListener(v -> addStudent());
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidMatric(String matric) {
        return matric.matches("\\d{6}"); //Matric number must be 6 digits
    }
    private void hideKeyboard() {
        InputMethodManager i = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            view = getWindow().getDecorView();
        }
        if (i != null && view != null) {
            i.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void addStudent() {
        String name = studentName.getText().toString().trim();
        String email = studentEmail.getText().toString().trim();
        String matric = studentMatric.getText().toString().trim();

        studentEmail.setError(null);
        studentMatric.setError(null);

        if (name.isEmpty() || email.isEmpty() || matric.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidEmail(email)) {
            studentEmail.setError("Invalid email (e.g., ana@example.com)");
        return;
        }
        if (!isValidMatric(matric)) {
            studentMatric.setError("Must be 6 digits");
            return;
        }
        hideKeyboard(); //Close keyboard
        btnAdd.setEnabled(false);
        studentViewModel.addStudentToCourse(name, email, matric, courseCode);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // go back
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void AddStudentbtn(View view) {
        //Handle onClick
        addStudent();
        finish(); //Return to course details
    }
}