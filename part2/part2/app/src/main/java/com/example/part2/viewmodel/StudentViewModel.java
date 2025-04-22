package com.example.part2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.part2.data.entities.Student;
import com.example.part2.data.repository.StudentRepository;

import java.util.List;

public class StudentViewModel extends AndroidViewModel {
    private final StudentRepository studentRepository;
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> studentAdded = new MutableLiveData<>();

    public StudentViewModel(@NonNull Application application) {
        super(application);
        studentRepository = new StudentRepository(application);
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public LiveData<Boolean> getStudentAdded() {
        return studentAdded;
    }

    private MutableLiveData<List<Student>> courseStudents = new MutableLiveData<>();

    public LiveData<List<Student>> getStudentsForCourse(int courseId) {
        loadStudentsForCourse(courseId);
        return courseStudents;
    }

    private void loadStudentsForCourse(int courseId) {
        studentRepository.getStudentsForCourse(courseId, new StudentRepository.RepositoryCallback<List<Student>>() {
            @Override
            public void onSuccess(List<Student> result) {
                courseStudents.postValue(result);
            }

            @Override
            public void onError(Exception e) {
                toastMessage.postValue("Error loading students");
            }
        });
    }

    public void addStudentToCourse(String name, String email, String username, int courseId) {
        if (name.isEmpty() || email.isEmpty() || username.isEmpty()) {
            toastMessage.setValue("All fields required");
            return;
        }

        studentRepository.getStudentByUsername(username, new StudentRepository.RepositoryCallback<Student>() {
            @Override
            public void onSuccess(Student existingStudent) {
                if (existingStudent != null) {
                    checkEnrollmentAndAdd(courseId, existingStudent, name, email);
                } else {
                    createNewStudent(courseId, name, email, username);
                }
            }

            @Override
            public void onError(Exception e) {
                toastMessage.setValue("Error checking student");
            }
        });
    }

    private void checkEnrollmentAndAdd(int courseId, Student student, String newName, String newEmail) {
        studentRepository.isStudentEnrolled(courseId, student.getStudentId(),
                new StudentRepository.RepositoryCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean isEnrolled) {
                        if (isEnrolled) {
                            toastMessage.setValue("Student is already enrolled");
                        } else {
                            updateStudentIfNeededAndEnroll(courseId, student, newName, newEmail);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        toastMessage.setValue("Error checking enrollment");
                    }
                });
    }

    private void updateStudentIfNeededAndEnroll(int courseId, Student student, String newName, String newEmail) {
        boolean needsUpdate = !student.getName().equals(newName) || !student.getEmail().equals(newEmail);

        if (needsUpdate) {
            student.setName(newName);
            student.setEmail(newEmail);
            // In a real app, you might want to update the student in the database here
        }

        enrollStudent(courseId, student.getStudentId());
    }

    private void createNewStudent(int courseId, String name, String email, String username) {
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setUserName(username);

        studentRepository.insertStudent(student, new StudentRepository.RepositoryCallback<Long>() {
            @Override
            public void onSuccess(Long studentId) {
                enrollStudent(courseId, studentId.intValue());
            }

            @Override
            public void onError(Exception e) {
                toastMessage.setValue("Error creating student");
            }
        });
    }

    private void enrollStudent(int courseId, int studentId) {
        studentRepository.enrollStudent(courseId, studentId,
                new StudentRepository.RepositoryCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        studentAdded.setValue(true);
                    }

                    @Override
                    public void onError(Exception e) {
                        toastMessage.setValue("Error enrolling student");
                    }
                });
    }
}
