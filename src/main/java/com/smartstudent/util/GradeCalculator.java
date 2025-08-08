/**
 * Grade Calculator Utility Class
 * 
 * Provides grade calculation and statistical analysis functionality.
 * This class handles grade computation, GPA calculation, and comprehensive
 * statistical analysis of student performance data.
 * 
 * Features:
 * - Grade calculation based on marks
 * - GPA computation with standard scale
 * - Pass/Fail determination
 * - Comprehensive statistical analysis
 * - Department and grade distribution analysis
 * 
 * @author Smart Student Management System
 * @version 1.0
 * @since 2025
 */
package com.smartstudent.util;

import com.smartstudent.model.Student;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GradeCalculator {
    
    /**
     * Calculates grade based on marks using standard grading scale
     * 
     * Converts numerical marks to letter grades using the following scale:
     * - A+: 90-100 marks (Excellent)
     * - A:  80-89 marks (Very Good)
     * - B:  70-79 marks (Good)
     * - C:  60-69 marks (Average)
     * - D:  50-59 marks (Below Average)
     * - F:  0-49 marks (Fail)
     * 
     * @param marks Student's numerical marks (0-100)
     * @return Letter grade (A+, A, B, C, D, or F)
     */
    public static String calculateGrade(double marks) {
        if (marks >= 90) return "A+";
        else if (marks >= 80) return "A";
        else if (marks >= 70) return "B";
        else if (marks >= 60) return "C";
        else if (marks >= 50) return "D";
        else return "F";
    }
    
    /**
     * Determines if a student has passed based on marks
     * 
     * Uses a standard passing threshold of 50 marks.
     * Students with 50 or more marks are considered to have passed.
     * 
     * @param marks Student's numerical marks
     * @return true if student passed (≥50 marks), false otherwise
     */
    public static boolean isPassed(double marks) {
        return marks >= 50;
    }
    
    /**
     * Calculates GPA (Grade Point Average) based on marks
     * 
     * Converts marks to GPA using a 4.0 scale:
     * - A+ (90-100): 4.0 GPA
     * - A  (80-89):  3.5 GPA
     * - B  (70-79):  3.0 GPA
     * - C  (60-69):  2.5 GPA
     * - D  (50-59):  2.0 GPA
     * - F  (0-49):   0.0 GPA
     * 
     * @param marks Student's numerical marks
     * @return GPA value (0.0 to 4.0)
     */
    public static double calculateGPA(double marks) {
        if (marks >= 90) return 4.0;
        else if (marks >= 80) return 3.5;
        else if (marks >= 70) return 3.0;
        else if (marks >= 60) return 2.5;
        else if (marks >= 50) return 2.0;
        else return 0.0;
    }
    
    /**
     * Statistics class for comprehensive student performance analysis
     * 
     * Provides detailed statistical information about student performance
     * including counts, percentages, averages, and distributions.
     */
    public static class Statistics {
        // Basic counts
        private int totalStudents;
        private int passedStudents;
        private int failedStudents;
        
        // Marks analysis
        private double averageMarks;
        private double highestMarks;
        private double lowestMarks;
        
        // Distribution analysis
        private Map<String, Long> departmentStats;
        private Map<String, Long> gradeStats;
        
        /**
         * Constructor - Calculates comprehensive statistics from student list
         * 
         * Analyzes the provided student list to compute all statistical measures
         * including counts, averages, distributions, and percentages.
         * 
         * @param students List of students to analyze
         */
        public Statistics(List<Student> students) {
            // Calculate basic counts
            this.totalStudents = students.size();
            this.passedStudents = (int) students.stream().filter(Student::isPassed).count();
            this.failedStudents = totalStudents - passedStudents;
            
            // Calculate marks statistics if students exist
            if (!students.isEmpty()) {
                this.averageMarks = students.stream().mapToDouble(Student::getMarks).average().orElse(0.0);
                this.highestMarks = students.stream().mapToDouble(Student::getMarks).max().orElse(0.0);
                this.lowestMarks = students.stream().mapToDouble(Student::getMarks).min().orElse(0.0);
                
                // Calculate department-wise distribution
                this.departmentStats = students.stream()
                    .collect(Collectors.groupingBy(Student::getDepartment, Collectors.counting()));
                
                // Calculate grade-wise distribution
                this.gradeStats = students.stream()
                    .collect(Collectors.groupingBy(Student::getGrade, Collectors.counting()));
            }
        }
        
        // ==================== GETTER METHODS ====================
        
        /**
         * Gets the total number of students
         * @return Total student count
         */
        public int getTotalStudents() { return totalStudents; }
        
        /**
         * Gets the number of students who passed
         * @return Count of passed students
         */
        public int getPassedStudents() { return passedStudents; }
        
        /**
         * Gets the number of students who failed
         * @return Count of failed students
         */
        public int getFailedStudents() { return failedStudents; }
        
        /**
         * Gets the average marks across all students
         * @return Average marks (0.0 if no students)
         */
        public double getAverageMarks() { return averageMarks; }
        
        /**
         * Gets the highest marks achieved by any student
         * @return Highest marks (0.0 if no students)
         */
        public double getHighestMarks() { return highestMarks; }
        
        /**
         * Gets the lowest marks achieved by any student
         * @return Lowest marks (0.0 if no students)
         */
        public double getLowestMarks() { return lowestMarks; }
        
        /**
         * Gets department-wise student distribution
         * @return Map of department names to student counts
         */
        public Map<String, Long> getDepartmentStats() { return departmentStats; }
        
        /**
         * Gets grade-wise student distribution
         * @return Map of grades to student counts
         */
        public Map<String, Long> getGradeStats() { return gradeStats; }
        
        /**
         * Calculates the percentage of students who passed
         * 
         * @return Pass percentage (0.0 to 100.0)
         */
        public double getPassPercentage() {
            return totalStudents > 0 ? (double) passedStudents / totalStudents * 100 : 0.0;
        }
        
        /**
         * Calculates the percentage of students who failed
         * 
         * @return Fail percentage (0.0 to 100.0)
         */
        public double getFailPercentage() {
            return totalStudents > 0 ? (double) failedStudents / totalStudents * 100 : 0.0;
        }
    }
    
    /**
     * Creates comprehensive statistics from a list of students
     * 
     * Analyzes the provided student list and returns a Statistics object
     * containing all calculated measures and distributions.
     * 
     * @param students List of students to analyze
     * @return Statistics object with comprehensive analysis
     */
    public static Statistics calculateStatistics(List<Student> students) {
        return new Statistics(students);
    }
} 