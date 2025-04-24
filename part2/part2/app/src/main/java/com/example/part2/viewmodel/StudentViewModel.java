package com.example.part2.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.part2.data.entities.Student;
import com.example.part2.data.entities.StudentWithCourses;
import com.example.part2.data.repository.StudentRepository;

import java.util.Collections;
import java.util.List;

// ViewModel for managing student-related data and interactions between UI and repository
public class StudentViewModel extends AndroidViewModel {

    private final StudentRepository studentRepository; // Handles data operations
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>(); // For displaying UI messages
    private final MutableLiveData<Boolean> studentAdded = new MutableLiveData<>(); // Signals if a student was added
    private final MutableLiveData<List<Student>> courseStudents = new MutableLiveData<>(); // List of students in a course

    // Constructor initializes the repository
    public StudentViewModel(@NonNull Application application) {
        super(application);
        studentRepository = new StudentRepository(application);
    }

    // Getters for LiveData observers in the UI
    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public LiveData<Boolean> getStudentAdded() {
        return studentAdded;
    }

    // Resets the flag to false after the student is added
    public void resetStudentAddedFlag() {
        studentAdded.setValue(false);
    }

    // Public method to trigger and observe students for a specific course
    public LiveData<List<Student>> getStudentsForCourse(String courseCode) {
        loadStudentsForCourse(courseCode);
        return courseStudents;
    }

    // Loads students enrolled in a course from the repository
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

    // Adds a student to a course after validation and checks
    public void addStudentToCourse(String name, String email, String matricNumber, String courseCode) {
        if (name.isEmpty() || email.isEmpty() || matricNumber.isEmpty()) {
            toastMessage.setValue("All fields are required");
            return;
        }

        String username = email.contains("@") ? email.substring(0, email.indexOf("@")) : email;

        // Checks if the student already exists by matric number
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
                toastMessage.postValue("Error checking student");
            }
        });
    }

    // Checks if a student is already enrolled and either enrolls or updates them
    private void checkEnrollmentAndAdd(String courseCode, Student student, String newName, String newEmail, String matricNumber) {
        studentRepository.getCourseId(courseCode, new StudentRepository.RepositoryCallback<>() {
            @Override
            public void onSuccess(Integer courseId) {
                studentRepository.isStudentEnrolled(courseId, student.getStudentId(), new StudentRepository.RepositoryCallback<>() {
                    @Override
                    public void onSuccess(Boolean isEnrolled) {
                        if (isEnrolled) {
                            toastMessage.postValue("Student is already enrolled");
                        } else {
                            updateStudentIfNeededAndEnroll(courseId, student, newName, newEmail, matricNumber);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        toastMessage.postValue("Error checking enrollment");
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                toastMessage.postValue("Invalid course");
            }
        });
    }

    // Updates student info if needed before enrolling them
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

    // Creates a new student and enrolls them in a course
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
                        toastMessage.postValue("Could not enroll student in course. Please try again.");
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                toastMessage.setValue("Error creating student");
            }
        });
    }

    // Enrolls the student in the specified course
    private void enrollStudent(int courseId, int studentId) {
        studentRepository.enrollStudent(courseId, studentId, new StudentRepository.RepositoryCallback<>() {
            @Override
            public void onSuccess(Void result) {
                toastMessage.postValue(null);
                studentAdded.postValue(true); // Notify that student was added
            }

            @Override
            public void onError(Exception e) {
                toastMessage.postValue("Error enrolling student");
            }
        });
    }

    // Retrieves a student with all their courses
    public LiveData<StudentWithCourses> getStudentWithCourses(int studentId) {
        return studentRepository.getStudentWithCourses(studentId);
    }

    // Retrieves a single student by ID
    public LiveData<Student> getStudentById(int studentId) {
        return studentRepository.getStudentById(studentId);
    }

    // Updates a student's info
    public void updateStudent(Student student) {
        studentRepository.updateStudent(student, new StudentRepository.RepositoryCallback<>() {
            @Override
            public void onSuccess(Void result) {
                toastMessage.postValue("Student updated successfully");
            }

            @Override
            public void onError(Exception e) {
                toastMessage.postValue("Error updating student");
            }
        });
    }

    // Unenrolls a student from a course and refreshes the student list
    public void unenrollStudent(int courseId, int studentId, String courseCode) {
        studentRepository.unenrollStudent(courseId, studentId, new StudentRepository.RepositoryCallback<>() {
            @Override
            public void onSuccess(Void result) {
                toastMessage.postValue("Student removed from course");
                loadStudentsForCourse(courseCode); // Refresh student list
            }

            @Override
            public void onError(Exception e) {
                toastMessage.postValue("Error removing student");
            }
        });
    }

    // Exposes course ID retrieval for external use
    public void getCourseId(String courseCode, StudentRepository.RepositoryCallback<Integer> callback) {
        studentRepository.getCourseId(courseCode, callback);
    }
}
