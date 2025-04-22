package com.example.part2.data.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Student {
    @PrimaryKey(autoGenerate = true)
    private int studentId;

    private String name;
    private String email;
    private String userName;

    // ✅ Constructor used manually (e.g., in AddStudentActivity)
    @Ignore
    public Student(String name, String email, String userName) {
        this.name = name;
        this.email = email;
        this.userName = userName;
    }

    // ✅ No-arg constructor required by Room
    public Student() {
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}