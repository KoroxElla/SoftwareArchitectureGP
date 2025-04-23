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

// Adapter class for displaying a list of Course items in a RecyclerView
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courses; // List of courses to display
    private OnCourseClickListener listener; // Listener for item interactions

    // Interface for click and long-click interactions on a course item
    public interface OnCourseClickListener {
        void onCourseClick(Course course); // Called on short click
        void onCourseLongClick(Course course); // Called on long click
    }

    // Constructor for initializing the adapter with course data and a listener
    public CourseAdapter(List<Course> courses, OnCourseClickListener listener) {
        this.courses = courses;
        this.listener = listener;
    }

    // Method to update the adapter's course list and notify changes
    public void setCourseList(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged(); // Refresh RecyclerView
    }

    // Called when a new ViewHolder is needed
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single course item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_course, parent, false);
        return new CourseViewHolder(view); // Return the new ViewHolder
    }

    // Binds data to the ViewHolder at the specified position
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position); // Get course at the current position
        holder.bind(course); // Bind data to the item view

        // Set short click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onCourseClick(course);
            }
        });

        // Set long click listener
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onCourseLongClick(course);
            }
            return true; // Return true to indicate the long click was handled
        });
    }


    // Returns the total number of course items
    @Override
    public int getItemCount() {
        return courses != null ? courses.size() : 0;
    }

    // ViewHolder class to hold references to the views in each course item layout
    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseCode, courseName, lecturerName;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find views by ID from the layout
            courseCode = itemView.findViewById(R.id.tvCourseCode);
            courseName = itemView.findViewById(R.id.tvCourseName);
            lecturerName = itemView.findViewById(R.id.tvLecturerName);
        }

        // Binds course data to the corresponding TextViews
        public void bind(Course course) {
            courseCode.setText(course.getCourseCode());
            courseName.setText(course.getCourseName());
            lecturerName.setText(course.getLecturerName());
        }
    }
}
