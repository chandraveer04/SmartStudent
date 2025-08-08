/**
 * Export Utility Class
 * 
 * Provides functionality to export student data in various formats.
 * This class handles CSV and text file exports with proper formatting
 * and data organization.
 * 
 * Features:
 * - CSV export with proper field escaping
 * - Formatted text export with detailed reports
 * - Automatic filename generation with timestamps
 * - Comprehensive data formatting
 * - Statistical summary generation
 * 
 * @author Smart Student Management System
 * @version 1.0
 * @since 2025
 */
package com.smartstudent.util;

import com.smartstudent.model.Student;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportUtil {
    
    /**
     * Exports student data to CSV format
     * 
     * Creates a CSV file with student information including grades and status.
     * The file is automatically named with a timestamp if no filename is provided.
     * 
     * CSV Format:
     * - Header row with column names
     * - Data rows with student information
     * - Proper CSV field escaping for special characters
     * 
     * @param students List of students to export
     * @param filename Custom filename (optional, auto-generated if null)
     * @throws IOException if file writing fails
     */
    public static void exportToCSV(List<Student> students, String filename) throws IOException {
        // Generate filename with timestamp if not provided
        if (filename == null || filename.trim().isEmpty()) {
            filename = "students_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write CSV header
            writer.println("ID,Name,Roll No,Department,Email,Phone,Marks,Grade,Status");
            
            // Write student data rows
            for (Student student : students) {
                writer.printf("%d,%s,%s,%s,%s,%s,%.2f,%s,%s%n",
                    student.getId(),
                    escapeCsvField(student.getName()),
                    escapeCsvField(student.getRollNo()),
                    escapeCsvField(student.getDepartment()),
                    escapeCsvField(student.getEmail()),
                    escapeCsvField(student.getPhone()),
                    student.getMarks(),
                    student.getGrade(),
                    student.isPassed() ? "Pass" : "Fail"
                );
            }
        }
        System.out.println("Data exported to CSV file: " + filename);
    }
    
    /**
     * Exports student data to formatted text format
     * 
     * Creates a detailed text report with student information and statistics.
     * The report includes a header, formatted table, and summary statistics.
     * 
     * Text Format:
     * - Report header with timestamp
     * - Formatted table with student data
     * - Summary statistics (total, passed, failed, averages)
     * - Professional formatting with proper spacing
     * 
     * @param students List of students to export
     * @param filename Custom filename (optional, auto-generated if null)
     * @throws IOException if file writing fails
     */
    public static void exportToText(List<Student> students, String filename) throws IOException {
        // Generate filename with timestamp if not provided
        if (filename == null || filename.trim().isEmpty()) {
            filename = "students_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write report header
            writer.println("STUDENT MANAGEMENT SYSTEM - EXPORT REPORT");
            writer.println("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("Total Students: " + students.size());
            writer.println("=".repeat(80));
            writer.println();
            
            // Write formatted table header
            writer.printf("%-5s %-20s %-10s %-20s %-25s %-15s %-8s %-6s %-6s%n",
                "ID", "Name", "Roll No", "Department", "Email", "Phone", "Marks", "Grade", "Status");
            writer.println("-".repeat(80));
            
            // Write student data rows
            for (Student student : students) {
                writer.printf("%-5d %-20s %-10s %-20s %-25s %-15s %-8.2f %-6s %-6s%n",
                    student.getId(),
                    truncate(student.getName(), 18),
                    student.getRollNo(),
                    truncate(student.getDepartment(), 18),
                    truncate(student.getEmail(), 23),
                    student.getPhone(),
                    student.getMarks(),
                    student.getGrade(),
                    student.isPassed() ? "Pass" : "Fail"
                );
            }
            
            // Write summary statistics
            writer.println();
            writer.println("=".repeat(80));
            writer.println("SUMMARY:");
            writer.println("Total Students: " + students.size());
            writer.println("Passed: " + students.stream().filter(Student::isPassed).count());
            writer.println("Failed: " + students.stream().filter(s -> !s.isPassed()).count());
            
            // Calculate and display statistical information
            if (!students.isEmpty()) {
                double avgMarks = students.stream().mapToDouble(Student::getMarks).average().orElse(0.0);
                double maxMarks = students.stream().mapToDouble(Student::getMarks).max().orElse(0.0);
                double minMarks = students.stream().mapToDouble(Student::getMarks).min().orElse(0.0);
                
                writer.println("Average Marks: " + String.format("%.2f", avgMarks));
                writer.println("Highest Marks: " + String.format("%.2f", maxMarks));
                writer.println("Lowest Marks: " + String.format("%.2f", minMarks));
            }
        }
        System.out.println("Data exported to text file: " + filename);
    }
    
    /**
     * Escapes CSV field values for proper formatting
     * 
     * Handles special characters in CSV fields by adding quotes and escaping
     * internal quotes. This ensures CSV compatibility across different applications.
     * 
     * @param field Field value to escape
     * @return Properly escaped CSV field value
     */
    private static String escapeCsvField(String field) {
        if (field == null) return "";
        
        // Check if field contains special characters that require quoting
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            // Escape internal quotes and wrap in quotes
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
    
    /**
     * Truncates text to fit within specified width
     * 
     * Cuts text to the specified maximum length and adds ellipsis
     * if the text is longer than the limit. Used for formatting
     * table columns in text exports.
     * 
     * @param str String to truncate
     * @param maxLength Maximum allowed length
     * @return Truncated string with ellipsis if needed
     */
    private static String truncate(String str, int maxLength) {
        if (str == null) return "";
        
        if (str.length() <= maxLength) {
            return str;
        }
        
        // Truncate and add ellipsis
        return str.substring(0, maxLength - 3) + "...";
    }
} 