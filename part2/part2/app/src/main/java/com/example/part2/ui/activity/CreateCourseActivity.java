package com.example.part2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.part2.R;
import com.example.part2.data.entities.Course;
import com.example.part2.viewmodel.CourseViewModel;

public class CreateCourseActivity extends AppCompatActivity {

    private CourseViewModel courseViewModel;
    private EditText courseCodeEditText;
    private EditText courseNameEditText;
    private EditText lecturerNameEditText;
    private Button submitButton;
    private TextView courseCodeErrorTextView; // Added this line

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_course);
        setTitle("Create a New Course");

        // Initialize Views
        courseCodeErrorTextView = findViewById(R.id.courseCodeErrorTextView); // Moved here
        courseCodeErrorTextView.setVisibility(View.GONE);  // Hide initially

        // Initialize ViewModel and other views
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseCodeEditText = findViewById(R.id.Coursecode);
        courseNameEditText = findViewById(R.id.Coursename);
        lecturerNameEditText = findViewById(R.id.Lecturername);
        submitButton = findViewById(R.id.button);

        // Edge to edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void returnhome(View view) {
        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        // Disable button to prevent multiple clicks
        submitButton.setEnabled(false);

        // Get and trim input values
        String coursecode = courseCodeEditText.getText().toString().trim();
        String coursename = courseNameEditText.getText().toString().trim();
        String lecturename = lecturerNameEditText.getText().toString().trim();

        // Log values exactly as entered (before validation)
        Log.d("Coursec", coursecode);
        Log.d("Coursen", coursename);
        Log.d("Lecturer", lecturename);

        // Validate all fields
        if (coursecode.isEmpty() || coursename.isEmpty() || lecturename.isEmpty()) {
            courseCodeErrorTextView.setText(R.string.all_fields_required);
            courseCodeErrorTextView.setVisibility(View.VISIBLE);
            submitButton.setEnabled(true); // Re-enable button
            return;
        }

        // Validate course code format (2 uppercase letters + 4 digits)
        if (!coursecode.matches("^[A-Z]{2}\\d{4}$")) { // Fixed typo: courseode -> coursecode
            courseCodeErrorTextView.setText(R.string.invalid_course_code);
            courseCodeErrorTextView.setVisibility(View.VISIBLE);
            submitButton.setEnabled(true); // Re-enable button
            return;
        }

        // Check uniqueness - this is where the real change happens
        courseViewModel.validateCourseCode(coursecode);
        courseViewModel.courseCodeError.observe(this, errorMessage -> {
            if (errorMessage != null) {
                courseCodeErrorTextView.setText(errorMessage);
                courseCodeErrorTextView.setVisibility(View.VISIBLE);
                submitButton.setEnabled(true);
            } else {
                // Only proceed if code is unique
                Course course = new Course(coursecode, coursename, lecturename);
                courseViewModel.addCourse(course);
                submitButton.setEnabled(true);

                // Log and navigate
                Log.d("CreateCourse", "Added course - Code: " + coursecode
                        + ", Name: " + coursename
                        + ", Lecturer: " + lecturename);

                startActivity(new Intent(this, MainActivity.class));
                finish(); // Prevent going back to this activity
            }
        });
    }
}
