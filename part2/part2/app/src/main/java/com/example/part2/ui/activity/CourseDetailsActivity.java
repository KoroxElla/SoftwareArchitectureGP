package com.example.part2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.part2.R;
import com.example.part2.ui.adapters.StudentAdapter;
import com.example.part2.viewmodel.StudentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CourseDetailsActivity extends AppCompatActivity {

    private RecyclerView studentsRecyclerView;
    private StudentAdapter studentAdapter;
    private StudentViewModel studentViewModel;
    private int courseId;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_details);

        // Get course ID from intent
        courseId = getIntent().getIntExtra("courseId", -1);

        setupActionBar();
        setupCustomTitle();
        setupLecturer();

        emptyView = findViewById(R.id.emptyView);

        // RecyclerView setup
        studentsRecyclerView = findViewById(R.id.courserecycler);
        studentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentAdapter(new ArrayList<>());
        studentsRecyclerView.setAdapter(studentAdapter);

        // ViewModel setup
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        observeStudents();

        // FAB to add a student
        FloatingActionButton addStudentButton = findViewById(R.id.addStudentButton);
        addStudentButton.setOnClickListener(v -> {
            Intent intent = new Intent(CourseDetailsActivity.this, AddStudentActivity.class);
            intent.putExtra("courseId", courseId);
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
        String courseCode = getIntent().getStringExtra("coursecode");
        String courseName = getIntent().getStringExtra("coursename");

        View customTitleView = getLayoutInflater().inflate(R.layout.custom_toolbar_title, null);
        TextView topTitle = customTitleView.findViewById(R.id.top_title);
        TextView mainTitle = customTitleView.findViewById(R.id.main_title);

        topTitle.setText(courseCode != null ? courseCode : "COURSE CODE");
        mainTitle.setText(courseName != null ? courseName : "COURSE NAME");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setCustomView(customTitleView,
                    new ActionBar.LayoutParams(
                            ActionBar.LayoutParams.WRAP_CONTENT,
                            ActionBar.LayoutParams.MATCH_PARENT
                    ));
        }
    }

    private void setupLecturer() {
        String lecturer = getIntent().getStringExtra("lecturer");
        TextView lecturerView = findViewById(R.id.Lecturername);

        if (lecturer != null && !lecturer.isEmpty()) {
            lecturerView.setText(lecturer);
        } else {
            lecturerView.setText("Lecturer not specified");
            lecturerView.setTextColor(ContextCompat.getColor(this, R.color.gray));
        }
    }

    private void observeStudents() {
        studentViewModel.getStudentsForCourse(courseId).observe(this, students -> {
            if (students != null) {
                studentAdapter.updateStudents(students);

                if (students.isEmpty()) {
                    studentsRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    studentsRecyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });

        // Observe for student added events
        studentViewModel.getStudentAdded().observe(this, added -> {
            if (added != null && added) {
                // Refresh the student list
                studentViewModel.loadStudentsForCourse(courseId);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
