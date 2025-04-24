package com.example.part2.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.part2.R;
import com.example.part2.data.entities.Student;

import java.util.List;

// Adapter class to bridge data (List<Student>) and RecyclerView
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<Student> students;  // List of students to display
    private final Context context;   // Context for accessing resources, etc.

    // Constructor to initialize adapter with list and context
    public StudentAdapter(List<Student> students, Context context) {
        this.students = students;
        this.context = context;
    }

    // Listener interface for handling item click events
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(Student student); // Callback when an item is clicked
    }

    // Setter to assign a click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    // Method to update student list and refresh RecyclerView
    public void setStudentList(List<Student> students) {
        this.students = students;
        notifyDataSetChanged(); // Refreshes the view
    }

    // Inflates the layout for each item and returns the ViewHolder
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false); // Inflate custom layout
        return new StudentViewHolder(view); // Return a new ViewHolder instance
    }

    // Binds data to each ViewHolder as the user scrolls
    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position); // Get student at current position
        holder.bind(student); // Bind student data to UI
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(student); // Invoke click callback
            }
        });
    }

    // Returns the number of items in the list
    @Override
    public int getItemCount() {
        return students != null ? students.size() : 0;
    }

    // ViewHolder class to hold item layout views
    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, studentEmail, studentMatric;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize TextViews using item layout IDs
            studentName = itemView.findViewById(R.id.student_name);
            studentEmail = itemView.findViewById(R.id.student_email);
            studentMatric = itemView.findViewById(R.id.student_matric);
        }

        // Binds a Student object to the TextViews
        public void bind(Student student){
            studentName.setText(student.getName());
            studentEmail.setText(student.getEmail());
            studentMatric.setText(student.getMatricNumber());
        }
    }
}
