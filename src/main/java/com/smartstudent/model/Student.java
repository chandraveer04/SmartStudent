/**
 * Student Model Class
 * 
 * Represents a student entity in the Smart Student Management System.
 * This class contains all the attributes and behaviors related to a student,
 * including personal information, academic details, and utility methods.
 * 
 * Features:
 * - Complete student information storage
 * - Grade calculation based on marks
 * - Pass/Fail status determination
 * - Timestamp tracking for audit trails
 * - Data validation and utility methods
 * 
 * @author Smart Student Management System
 * @version 1.0
 * @since 2025
 */
package com.smartstudent.model;

import java.time.LocalDateTime;

public class Student {
    // Database primary key
    private int id;
    
    // Personal information
    private String name;
    private String rollNo;
    private String department;
    private String email;
    private String phone;
    
    // Academic information
    private double marks;
    
    // Audit timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Default constructor for creating empty student objects
     */
    public Student() {}

    /**
     * Constructor for creating new student records (without ID)
     * 
     * @param name Student's full name
     * @param rollNo Student's unique roll number
     * @param department Student's department
     * @param email Student's email address
     * @param phone Student's phone number
     * @param marks Student's academic marks
     */
    public Student(String name, String rollNo, String department, String email, String phone, double marks) {
        this.name = name;
        this.rollNo = rollNo;
        this.department = department;
        this.email = email;
        this.phone = phone;
        this.marks = marks;
    }

    /**
     * Constructor for creating student records with database ID
     * 
     * @param id Database primary key
     * @param name Student's full name
     * @param rollNo Student's unique roll number
     * @param department Student's department
     * @param email Student's email address
     * @param phone Student's phone number
     * @param marks Student's academic marks
     */
    public Student(int id, String name, String rollNo, String department, String email, String phone, double marks) {
        this.id = id;
        this.name = name;
        this.rollNo = rollNo;
        this.department = department;
        this.email = email;
        this.phone = phone;
        this.marks = marks;
    }

    // ==================== GETTERS AND SETTERS ====================
    
    /**
     * Gets the database primary key
     * @return Student ID
     */
    public int getId() { return id; }
    
    /**
     * Sets the database primary key
     * @param id Student ID
     */
    public void setId(int id) { this.id = id; }

    /**
     * Gets the student's full name
     * @return Student name
     */
    public String getName() { return name; }
    
    /**
     * Sets the student's full name
     * @param name Student name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the student's unique roll number
     * @return Roll number
     */
    public String getRollNo() { return rollNo; }
    
    /**
     * Sets the student's unique roll number
     * @param rollNo Roll number
     */
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    /**
     * Gets the student's department
     * @return Department name
     */
    public String getDepartment() { return department; }
    
    /**
     * Sets the student's department
     * @param department Department name
     */
    public void setDepartment(String department) { this.department = department; }

    /**
     * Gets the student's email address
     * @return Email address
     */
    public String getEmail() { return email; }
    
    /**
     * Sets the student's email address
     * @param email Email address
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Gets the student's phone number
     * @return Phone number
     */
    public String getPhone() { return phone; }
    
    /**
     * Sets the student's phone number
     * @param phone Phone number
     */
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * Gets the student's academic marks
     * @return Marks (0-100)
     */
    public double getMarks() { return marks; }
    
    /**
     * Sets the student's academic marks
     * @param marks Marks (0-100)
     */
    public void setMarks(double marks) { this.marks = marks; }

    /**
     * Gets the record creation timestamp
     * @return Creation timestamp
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    /**
     * Sets the record creation timestamp
     * @param createdAt Creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * Gets the record last update timestamp
     * @return Last update timestamp
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    /**
     * Sets the record last update timestamp
     * @param updatedAt Last update timestamp
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ==================== UTILITY METHODS ====================
    
    /**
     * Calculates and returns the grade based on marks
     * 
     * Grade Scale:
     * - A+: 90-100 marks
     * - A:  80-89 marks
     * - B:  70-79 marks
     * - C:  60-69 marks
     * - D:  50-59 marks
     * - F:  0-49 marks
     * 
     * @return Grade string (A+, A, B, C, D, or F)
     */
    public String getGrade() {
        if (marks >= 90) return "A+";
        else if (marks >= 80) return "A";
        else if (marks >= 70) return "B";
        else if (marks >= 60) return "C";
        else if (marks >= 50) return "D";
        else return "F";
    }

    /**
     * Determines if the student has passed based on marks
     * 
     * Passing criteria: 50 marks or above
     * 
     * @return true if student passed, false otherwise
     */
    public boolean isPassed() {
        return marks >= 50;
    }

    // ==================== OVERRIDE METHODS ====================
    
    /**
     * Returns a string representation of the student
     * 
     * @return Formatted string with student details
     */
    @Override
    public String toString() {
        return String.format("Student{id=%d, name='%s', rollNo='%s', department='%s', marks=%.2f}", 
                           id, name, rollNo, department, marks);
    }

    /**
     * Compares this student with another object for equality
     * 
     * Students are considered equal if they have the same roll number
     * 
     * @param obj Object to compare with
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return rollNo.equals(student.rollNo);
    }

    /**
     * Generates hash code based on roll number
     * 
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        return rollNo.hashCode();
    }
} 