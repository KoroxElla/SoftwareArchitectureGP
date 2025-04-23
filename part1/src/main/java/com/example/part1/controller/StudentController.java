package com.example.part1.controller;

import com.example.part1.domain.Student;
import com.example.part1.repo.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepo;

    public StudentController(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    // ✅ View student details
    @GetMapping("/{username}")
    public ResponseEntity<Student> getStudent(@PathVariable String username) {
        Optional<Student> student = studentRepo.findByUserName(username);
        return student.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Edit/update student details
    @PutMapping("/{username}")
    public ResponseEntity<String> updateStudent(@PathVariable String username, @RequestBody Student updated) {
        return studentRepo.findByUserName(username)
                .map(student -> {
                    student.setName(updated.getName());
                    student.setEmail(updated.getEmail());
                    studentRepo.save(student);
                    return ResponseEntity.ok("Student updated.");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Remove student from course
    @DeleteMapping("/{username}/courses/{courseId}")
    public ResponseEntity<String> removeStudentFromCourse(@PathVariable String username, @PathVariable Long courseId) {
        Optional<Student> studentOpt = studentRepo.findByUserName(username);
        if (studentOpt.isEmpty()) return ResponseEntity.notFound().build();

        Student student = studentOpt.get();
        student.getCourses().removeIf(course -> course.getCourseId().equals(courseId));
        studentRepo.save(student);

        return ResponseEntity.ok("Student removed from course.");
    }
}