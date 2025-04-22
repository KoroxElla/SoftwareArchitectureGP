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

public class CourseInStudentAdapter extends RecyclerView.Adapter<CourseInStudentAdapter.CourseViewHolder> {

    private List<Course> courseList;

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_in_student, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.courseName.setText(course.getCourseName());
        holder.courseCode.setText(course.getCourseCode());
        holder.lecturerName.setText(course.getLecturerName());
    }

    @Override
    public int getItemCount() {
        return courseList == null ? 0 : courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, courseCode, lecturerName;

        CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseNameText);
            courseCode = itemView.findViewById(R.id.courseCodeText);
            lecturerName = itemView.findViewById(R.id.lecturerText);
        }
    }
}