package com.example.part2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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
    private EditText courseCodeEditText, courseNameEditText, lecturerNameEditText;
    private Button submitButton;
    private TextView courseCodeErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_course);
        setTitle("Create a New Course");

        // Initialize views
        courseCodeErrorTextView = findViewById(R.id.courseCodeErrorTextView);
        courseCodeErrorTextView.setVisibility(View.GONE);

        // Initialize ViewModel
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // Bind views
        courseCodeEditText = findViewById(R.id.Coursecode);
        courseNameEditText = findViewById(R.id.Coursename);
        lecturerNameEditText = findViewById(R.id.Lecturername);
        submitButton = findViewById(R.id.button);

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Validates and submits new course data
     */
    public void returnhome(View view) {
        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        submitButton.setEnabled(false); // Prevent double clicks

        // Get trimmed input values
        String coursecode = courseCodeEditText.getText().toString().trim();
        String coursename = courseNameEditText.getText().toString().trim();
        String lecturename = lecturerNameEditText.getText().toString().trim();

        // Validate required fields
        if (coursecode.isEmpty() || coursename.isEmpty() || lecturename.isEmpty()) {
            showError(R.string.all_fields_required);
            return;
        }

        // Validate course code format (2 letters + 4 digits)
        if (!coursecode.matches("^[A-Z]{2}\\d{4}$")) {
            showError(R.string.invalid_course_code);
            return;
        }

        // Check for unique course code
        courseViewModel.validateCourseCode(coursecode);
        courseViewModel.courseCodeError.observe(this, errorMessage -> {
            if (errorMessage != null) {
                showError(errorMessage);
            } else {
                // Create and save new course
                Course course = new Course(coursecode, coursename, lecturename);
                courseViewModel.addCourse(course);

                // Navigate back to MainActivity
                startActivity(new Intent(this, MainActivity.class));
                finish(); // Prevent going back to this activity
            }
        });
    }

    /**
     * Shows error message in UI
     */
    private void showError(int messageResId) {
        courseCodeErrorTextView.setText(messageResId);
        courseCodeErrorTextView.setVisibility(View.VISIBLE);
        submitButton.setEnabled(true);
    }

    private void showError(String message) {
        courseCodeErrorTextView.setText(message);
        courseCodeErrorTextView.setVisibility(View.VISIBLE);
        submitButton.setEnabled(true);
    }
}
