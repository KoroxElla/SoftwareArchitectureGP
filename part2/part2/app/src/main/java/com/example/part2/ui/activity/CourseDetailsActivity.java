package com.example.part2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.part2.R;
import com.example.part2.data.entities.Student;
import com.example.part2.data.repository.StudentRepository;
import com.example.part2.ui.adapters.StudentAdapter;
import com.example.part2.viewmodel.StudentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CourseDetailsActivity extends AppCompatActivity {
    private RecyclerView studentsRecyclerView;
    private StudentAdapter studentAdapter;
    private StudentViewModel studentViewModel;
    private String courseCode; // Current course being viewed
    private TextView emptyView; // Shown when no students

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_details);

        // Get course data from intent
        courseCode = getIntent().getStringExtra("courseCode");
        if (courseCode == null || courseCode.isEmpty()) {
            Toast.makeText(this, "Invalid course", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel and RecyclerView
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        studentsRecyclerView = findViewById(R.id.courserecycler);
        studentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentAdapter(new ArrayList<>(), this);
        studentsRecyclerView.setAdapter(studentAdapter);

        emptyView = findViewById(R.id.emptyView);

        // Setup UI components
        setupActionBar();
        setupCustomTitle();
        setupLecturer();
        observeStudents();

        // Configure FAB
        FloatingActionButton addStudentButton = findViewById(R.id.addStudentButton);
        addStudentButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddStudentActivity.class);
            intent.putExtra("courseCode", courseCode);
            startActivity(intent);
        });

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Configures custom action bar
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Sets up custom title view with course code and name
     */
    private void setupCustomTitle() {
        String courseName = getIntent().getStringExtra("courseName");
        View customTitleView = getLayoutInflater().inflate(R.layout.custom_toolbar_title, null);

        TextView topTitle = customTitleView.findViewById(R.id.top_title);
        TextView mainTitle = customTitleView.findViewById(R.id.main_title);
        topTitle.setText(courseCode);
        mainTitle.setText(courseName != null ? courseName : "COURSE NAME");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setCustomView(customTitleView);
        }
    }

    /**
     * Displays lecturer name or placeholder text
     */
    private void setupLecturer() {
        String lecturer = getIntent().getStringExtra("lecturer");
        TextView lecturerView = findViewById(R.id.Lecturername);

        if (lecturer != null && !lecturer.isEmpty()) {
            lecturerView.setText(lecturer);
        } else {
            lecturerView.setText(R.string.lecturer_not_specified);
            lecturerView.setTextColor(ContextCompat.getColor(this, R.color.gray));
        }
    }

    /**
     * Observes student list changes and updates UI
     */
    private void observeStudents() {
        TextView studentsHeading = findViewById(R.id.studentsHeading);
        studentViewModel.getStudentsForCourse(courseCode).observe(this, students -> {
            if (students != null && !students.isEmpty()) {
                // Show student list
                studentsHeading.setVisibility(View.VISIBLE);
                studentAdapter.setStudentList(students);
                studentsRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            } else {
                // Show empty state
                studentsHeading.setVisibility(View.GONE);
                studentsRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        });

        // Set click listener for student items
        studentAdapter.setOnItemClickListener(this::showStudentOptionsDialog);

        // Observe toast messages
        studentViewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Shows options dialog for a student (View/Edit/Remove)
     */
    private void showStudentOptionsDialog(Student student) {
        new AlertDialog.Builder(this)
                .setTitle("Student Options")
                .setItems(new String[]{"View Details", "Edit", "Remove"}, (dialog, which) -> {
                    switch (which) {
                        case 0: viewStudentDetails(student); break;
                        case 1: editStudent(student); break;
                        case 2: removeStudentFromCourse(student); break;
                    }
                }).show();
    }

    private void viewStudentDetails(Student student) {
        Intent intent = new Intent(this, StudentDetailsActivity.class);
        intent.putExtra("studentId", student.getStudentId());
        intent.putExtra("courseCode", courseCode);
        intent.putExtra("courseName", getIntent().getStringExtra("courseName"));
        intent.putExtra("lecturer", getIntent().getStringExtra("lecturer"));
        startActivity(intent);
    }

    private void editStudent(Student student) {
        Intent intent = new Intent(this, EditStudentActivity.class);
        intent.putExtra("studentId", student.getStudentId());
        intent.putExtra("courseCode", courseCode);
        startActivity(intent);
    }

    /**
     * Removes student from current course
     */
    private void removeStudentFromCourse(Student student) {
        studentViewModel.getCourseId(courseCode, new StudentRepository.RepositoryCallback<>() {
            @Override
            public void onSuccess(Integer courseId) {
                if (courseId != null && courseId > 0) {
                    studentViewModel.unenrollStudent(courseId, student.getStudentId(), courseCode);
                } else {
                    Toast.makeText(CourseDetailsActivity.this,
                            "Could not determine course", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(CourseDetailsActivity.this,
                        "Error removing student: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        studentViewModel.loadStudentsForCourse(courseCode); // Refresh data
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}