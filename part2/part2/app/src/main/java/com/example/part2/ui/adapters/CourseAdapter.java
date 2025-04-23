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

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courses;
    private OnCourseClickListener listener;

    public interface OnCourseClickListener {
        void onCourseClick(Course course);
        void onCourseLongClick(Course course); // <-- Add this
    }

    public CourseAdapter(List<Course> courses, OnCourseClickListener listener) {
        this.courses = courses;
        this.listener = listener;
    }

    public void setCourseList(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.bind(course); // This sets the text values

        // This handles the regular clicks
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onCourseClick(course);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onCourseLongClick(course);
            }
            return true;
        });
    }

    public Course getCourseAt(int position) {
        if (position >= 0 && position < courses.size()) {
            return courses.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return courses != null ? courses.size() : 0;
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseCode, courseName, lecturerName;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseCode = itemView.findViewById(R.id.tvCourseCode);
            courseName = itemView.findViewById(R.id.tvCourseName);
            lecturerName = itemView.findViewById(R.id.tvLecturerName);
        }

        public void bind(Course course) {
            String template = "%s: %s";
            courseCode.setText(String.format(course.getCourseCode()));
            courseName.setText(String.format(course.getCourseName()));
            lecturerName.setText(String.format(course.getLecturerName()));
        }
    }
}