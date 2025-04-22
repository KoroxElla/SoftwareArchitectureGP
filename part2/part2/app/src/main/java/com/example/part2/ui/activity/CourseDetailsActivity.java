package com.example.part2.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.part2.R;
import com.example.part2.data.entities.Student;
import com.example.part2.ui.adapters.StudentAdapter;
import com.example.part2.viewmodel.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CourseDetailsActivity extends AppCompatActivity {

    private RecyclerView studentRecyclerView;
    private FloatingActionButton addStudentBtn;
    private StudentAdapter studentAdapter;
    private CourseViewModel courseViewModel;
    private int selectedCourseId;  // Use int instead of String

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        // Get the courseId passed from intent
        selectedCourseId = getIntent().getIntExtra("courseId", -1);

        studentRecyclerView = findViewById(R.id.studentRecyclerView);
        addStudentBtn = findViewById(R.id.addStudentButton);

        studentAdapter = new StudentAdapter();
        studentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentRecyclerView.setAdapter(studentAdapter);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // Load students enrolled in this course
        courseViewModel.getStudentsInCourse(selectedCourseId).observe(this, students -> {
            studentAdapter.setStudentList(students);
        });

        // Add student to course
        addStudentBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CourseDetailsActivity.this, AddStudentActivity.class);
            intent.putExtra("courseId", selectedCourseId);
            startActivity(intent);
        });

        // Handle student click (Edit or Remove)
        studentAdapter.setOnItemClickListener(student -> showStudentOptionsDialog(student));
    }

    private void showStudentOptionsDialog(Student student) {
        new AlertDialog.Builder(this)
                .setTitle("Choose an action for " + student.getName())
                .setItems(new CharSequence[]{"Edit", "Remove"}, (dialog, which) -> {
                    if (which == 0) {
                        // Edit student
                        Intent intent = new Intent(this, EditStudentActivity.class);
                        intent.putExtra("studentUserName", student.getUserName());
                        startActivity(intent);
                    } else {
                        // Remove student from this course
                        courseViewModel.unenrollStudentFromCourse(selectedCourseId, student.getStudentId());
                        Toast.makeText(this, "Student removed from course", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}