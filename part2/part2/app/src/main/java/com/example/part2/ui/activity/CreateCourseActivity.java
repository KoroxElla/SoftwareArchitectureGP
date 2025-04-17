package com.example.part2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_course);
        setTitle("Create a New Course");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    public void returnhome(View view) {
        String coursecode = ((TextView)findViewById(R.id.Coursecode)).getText().toString();
        String coursename = ((TextView)findViewById(R.id.Coursename)).getText().toString();
        String lecturename = ((TextView)findViewById(R.id.Lecturername)).getText().toString();

        CourseViewModel viewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        Course course = new Course(coursecode,coursename,lecturename);
        viewModel.addCourse(course);
        Log.d("Courec",coursecode);
        Log.d("CourseN",coursename);
        Log.d("Lecturer",lecturename);
        // Navigate back to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}