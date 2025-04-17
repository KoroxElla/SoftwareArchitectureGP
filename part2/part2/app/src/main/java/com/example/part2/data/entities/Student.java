package com.example.part2.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Student {
    @PrimaryKey(autoGenerate = true)
    private int studentId;

    private String name;
    private String email;
    private String userName;
}
