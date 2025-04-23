package com.example.part2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.part2.R;
import com.example.part2.data.entities.Student;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<Student> students;
    private OnStudentRemoveListener listener;

    public StudentAdapter(List<Student> students) {
        this.students = students;
    }

    public void updateStudents(List<Student> newStudents) {
        this.students = newStudents;
        notifyDataSetChanged();
    }
    public void setOnStudentRemoveListener (OnStudentRemoveListener listener) {
        this.listener = listener;
    }
    public interface OnStudentRemoveListener {
        void onRemoveClicked(String studentMatric);
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position);
        holder.bind(student);
        holder.removeBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveClicked(student.getMatricNumber());
            }
        });
    }

    @Override
    public int getItemCount() {
        return students != null ? students.size() : 0;
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, studentEmail, studentMatric;
        Button removeBtn;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.student_name);
            studentEmail = itemView.findViewById(R.id.student_email);
            studentMatric = itemView.findViewById(R.id.student_matric);
            removeBtn = itemView.findViewById(R.id.btnRemoveStudent);
        }

        public void bind(Student student) {
            studentName.setText(student.getName());
            studentEmail.setText(student.getEmail());
            studentMatric.setText(student.getMatricNumber());
        }
    }
}