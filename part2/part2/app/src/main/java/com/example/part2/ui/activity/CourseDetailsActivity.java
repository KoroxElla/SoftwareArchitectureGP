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
    private String courseCode;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_details);

        courseCode = getIntent().getStringExtra("courseCode");
        if (courseCode == null || courseCode.isEmpty()) {
            Toast.makeText(this, "Invalid course", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        studentsRecyclerView = findViewById(R.id.courserecycler);
        studentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentAdapter(new ArrayList<>(), this);
        studentsRecyclerView.setAdapter(studentAdapter);

        //Delete student
        studentAdapter.setOnStudentRemoveListener(matricNumber -> {
            studentViewModel.removeStudentFromCourse(courseCode, matricNumber);
            studentViewModel.loadStudentsForCourse(courseCode); //refresh list
        });

        emptyView = findViewById(R.id.emptyView);
        setupActionBar();
        setupCustomTitle();
        setupLecturer();
        observeStudents();

        FloatingActionButton addStudentButton = findViewById(R.id.addStudentButton);
        addStudentButton.setOnClickListener(v -> {
            Intent intent = new Intent(CourseDetailsActivity.this, AddStudentActivity.class);
            intent.putExtra("courseCode", courseCode);

            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupCustomTitle() {
        String courseName = getIntent().getStringExtra("courseName");
        View customTitleView = getLayoutInflater().inflate(R.layout.custom_toolbar_title,  studentsRecyclerView, false);
        TextView topTitle = customTitleView.findViewById(R.id.top_title);
        TextView mainTitle = customTitleView.findViewById(R.id.main_title);

        topTitle.setText(courseCode);
        mainTitle.setText(courseName != null ? courseName : "COURSE NAME");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setCustomView(customTitleView);
        }
    }

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

    private void observeStudents() {
        studentViewModel.getStudentsForCourse(courseCode).observe(this, students -> {
            if (students != null) {
                studentAdapter = new StudentAdapter(students, this);
                studentsRecyclerView.setAdapter(studentAdapter);

                // Set click listener for student items
                studentAdapter.setOnItemClickListener(student -> {
                    showStudentOptionsDialog(student);
                });

                if (students.isEmpty()) {
                    studentsRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    studentsRecyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });

        studentViewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showStudentOptionsDialog(Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Student Options")
                .setItems(new String[]{"View Details", "Edit", "Remove"}, (dialog, which) -> {
                    switch (which) {
                        case 0: // View Details
                            viewStudentDetails(student);
                            break;
                        case 1: // Edit
                            editStudent(student);
                            break;
                        case 2: // Remove
                            removeStudentFromCourse(student);
                            break;
                    }
                });
        builder.create().show();
    }

    private void viewStudentDetails(Student student) {
        Intent intent = new Intent(this, StudentDetailsActivity.class);
        intent.putExtra("studentId", student.getStudentId());
        intent.putExtra("courseCode", courseCode); // pass along the current course data
        intent.putExtra("courseName", getIntent().getStringExtra("courseName"));
        intent.putExtra("lecturer", getIntent().getStringExtra("lecturer"));
        startActivity(intent);
    }


    private void editStudent(Student student) {
        Intent intent = new Intent(this, EditStudentActivity.class);
        intent.putExtra("studentId", student.getStudentId());
        intent.putExtra("courseCode", courseCode); // pass along the current course data
        intent.putExtra("courseName", getIntent().getStringExtra("courseName"));
        intent.putExtra("lecturer", getIntent().getStringExtra("lecturer"));
        startActivity(intent);
    }

    private void removeStudentFromCourse(Student student) {
        studentViewModel.getCourseId(courseCode, new StudentRepository.RepositoryCallback<Integer>() {
            @Override
            public void onSuccess(Integer courseId) {
                if (courseId != null && courseId > 0) {
                    studentViewModel.unenrollStudent(courseId, student.getStudentId());
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
}