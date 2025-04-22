package com.example.part2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.part2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CourseDetailsActivity extends AppCompatActivity {
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        //Get courseID
        courseId = getIntent().getIntExtra("courseId", -1);
        FloatingActionButton addStudentButton = findViewById(R.id.addStudentButton);
        addStudentButton.setOnClickListener(v -> {
            Intent intent = new Intent(CourseDetailsActivity.this, AddStudentActivity.class);
            intent.putExtra("courseId", courseId);
            startActivity(intent);
        });
    }
}