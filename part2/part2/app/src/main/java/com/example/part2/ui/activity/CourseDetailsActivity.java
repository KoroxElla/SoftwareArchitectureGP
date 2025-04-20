package com.example.part2.ui.activity;

import static androidx.core.content.ContextCompat.startActivity;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CourseDetailsActivity {

    FloatingActionButton addStudentBtn = findViewById(R.id.addStudentButton);
    addStudentBtn.setOnClickListener(v -> {
        Intent intent = new Intent(CourseDetailsActivity.this, AddStudentActivity.class);
        intent.putExtra("courseId", selectedCourseId);
        startActivity(intent);
    });
}
