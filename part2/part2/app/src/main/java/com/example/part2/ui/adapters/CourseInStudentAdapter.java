package com.example.part2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.part2.R;
import com.example.part2.data.entities.Course;

import java.util.List;

// RecyclerView Adapter to display a list of Course objects in a student's profile
public class CourseInStudentAdapter extends RecyclerView.Adapter<CourseInStudentAdapter.CourseViewHolder> {

    private List<Course> courseList; // List of courses to display

    // Constructor to initialize the adapter with a list of courses
    public CourseInStudentAdapter(List<Course> courseList) {
        this.courseList = courseList;
    }


    // Called when a new ViewHolder is needed (creates/inflates item layout)
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for a course inside the student view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_in_student, parent, false);
        return new CourseViewHolder(view); // Return the new ViewHolder
    }

    // Binds course data to the views inside the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position); // Get course at the current position

        // Populate TextViews with course details
        holder.courseName.setText(course.getCourseName());
        holder.courseCode.setText(course.getCourseCode());
        holder.lecturerName.setText(course.getLecturerName());
    }

    // Returns the total number of items to be displayed
    @Override
    public int getItemCount() {
        return courseList == null ? 0 : courseList.size(); // Handle null case
    }

    // ViewHolder class to hold references to the views in each item layout
    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, courseCode, lecturerName;

        // Constructor that initializes the TextViews by finding them in the layout
        CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseNameText);
            courseCode = itemView.findViewById(R.id.courseCodeText);
            lecturerName = itemView.findViewById(R.id.lecturerText);
        }
    }
}
