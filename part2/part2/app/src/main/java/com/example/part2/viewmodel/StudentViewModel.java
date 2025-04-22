package com.example.part2.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.part2.data.entities.Student;
import com.example.part2.data.repository.StudentRepository;

import java.util.Collections;
import java.util.List;

public class StudentViewModel extends AndroidViewModel {
    private final StudentRepository studentRepository;
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> studentAdded = new MutableLiveData<>();
    private final MutableLiveData<List<Student>> courseStudents = new MutableLiveData<>();

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

    public LiveData<List<Student>> getStudentsForCourse(String courseCode) {
        loadStudentsForCourse(courseCode);
        return courseStudents;
    }

    public void loadStudentsForCourse(String courseCode) {
        studentRepository.getStudentsForCourse(courseCode, new StudentRepository.RepositoryCallback<>() {
            @Override
            public void onSuccess(List<Student> result) {
                courseStudents.postValue(result);
            }

            @Override
            public void onError(Exception e) {
                toastMessage.postValue("Error loading students");
                courseStudents.postValue(Collections.emptyList());
            }
        });
    }

    public void addStudentToCourse(String name, String email, String matricNumber, String courseCode) {
        if (name.isEmpty() || email.isEmpty() || matricNumber.isEmpty()) {
            toastMessage.setValue("All fields are required");
            return;
        }

        String username = email.contains("@") ? email.substring(0, email.indexOf("@")) : email;

        studentRepository.getStudentByMatric(matricNumber, new StudentRepository.RepositoryCallback<>() {
            @Override
            public void onSuccess(Student existingStudent) {
                if (existingStudent != null) {
                    checkEnrollmentAndAdd(courseCode, existingStudent, name, email, matricNumber);
                } else {
                    createNewStudent(courseCode, name, email, matricNumber, username);
                }
            }

            @Override
            public void onError(Exception e) {
                toastMessage.setValue("Error checking student");
            }
        });
    }

    private void checkEnrollmentAndAdd(String courseCode, Student student, String newName, String newEmail, String matricNumber) {
        studentRepository.getCourseId(courseCode, new StudentRepository.RepositoryCallback<>() {
            @Override
            public void onSuccess(Integer courseId) {
                studentRepository.isStudentEnrolled(courseId, student.getStudentId(),
                        new StudentRepository.RepositoryCallback<>() {
                            @Override
                            public void onSuccess(Boolean isEnrolled) {
                                if (isEnrolled) {
                                    toastMessage.setValue("Student is already enrolled");
                                } else {
                                    updateStudentIfNeededAndEnroll(courseId, student, newName, newEmail, matricNumber);
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                toastMessage.setValue("Error checking enrollment");
                            }
                        });
            }

            @Override
            public void onError(Exception e) {
                toastMessage.setValue("Invalid course");
            }
        });
    }

    private void updateStudentIfNeededAndEnroll(int courseId, Student student, String newName, String newEmail, String matricNumber) {
        boolean needsUpdate = !student.getName().equals(newName) ||
                !student.getEmail().equals(newEmail) ||
                !student.getMatricNumber().equals(matricNumber);

        if (needsUpdate) {
            student.setName(newName);
            student.setEmail(newEmail);
            student.setMatricNumber(matricNumber);
            studentRepository.updateStudent(student, new StudentRepository.RepositoryCallback<>() {
                @Override
                public void onSuccess(Void result) {
                    enrollStudent(courseId, student.getStudentId());
                }

                @Override
                public void onError(Exception e) {
                    toastMessage.setValue("Error updating student");
                }
            });
        } else {
            enrollStudent(courseId, student.getStudentId());
        }
    }

    private void createNewStudent(String courseCode, String name, String email, String matricNumber, String username) {
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setMatricNumber(matricNumber);
        student.setUserName(username);

        studentRepository.insertStudent(student, new StudentRepository.RepositoryCallback<>() {
            @Override
            public void onSuccess(Long studentId) {
                studentRepository.getCourseId(courseCode, new StudentRepository.RepositoryCallback<>() {
                    @Override
                    public void onSuccess(Integer courseId) {
                        enrollStudent(courseId, studentId.intValue());
                    }

                    @Override
                    public void onError(Exception e) {
                        toastMessage.setValue("Error getting course ID");
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                toastMessage.setValue("Error creating student");
            }
        });
    }

    private void enrollStudent(int courseId, int studentId) {
        studentRepository.enrollStudent(courseId, studentId,
                new StudentRepository.RepositoryCallback<>() {
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