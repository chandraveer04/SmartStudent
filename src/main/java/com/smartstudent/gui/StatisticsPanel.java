package com.smartstudent.gui;

import com.smartstudent.dao.StudentDAO;
import com.smartstudent.model.Student;
import com.smartstudent.util.GradeCalculator;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class StatisticsPanel extends JPanel {
    private StudentDAO studentDAO;
    private JLabel totalStudentsLabel;
    private JLabel passedStudentsLabel;
    private JLabel failedStudentsLabel;
    private JLabel averageMarksLabel;
    private JLabel highestMarksLabel;
    private JLabel lowestMarksLabel;
    private JTextArea departmentStatsArea;
    private JTextArea gradeStatsArea;
    private JButton refreshButton;
    private JButton exportButton;

    public StatisticsPanel() {
        studentDAO = new StudentDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadStatistics();
    }

    private void initializeComponents() {
        totalStudentsLabel = new JLabel("Total Students: 0");
        passedStudentsLabel = new JLabel("Passed: 0 (0.0%)");
        failedStudentsLabel = new JLabel("Failed: 0 (0.0%)");
        averageMarksLabel = new JLabel("Average Marks: 0.00");
        highestMarksLabel = new JLabel("Highest Marks: 0.00");
        lowestMarksLabel = new JLabel("Lowest Marks: 0.00");

        departmentStatsArea = new JTextArea(10, 30);
        departmentStatsArea.setEditable(false);
        departmentStatsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        gradeStatsArea = new JTextArea(10, 30);
        gradeStatsArea.setEditable(false);
        gradeStatsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        refreshButton = new JButton("Refresh Statistics");
        exportButton = new JButton("Export Statistics");

        // Style buttons
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);

        exportButton.setBackground(new Color(34, 139, 34));
        exportButton.setForeground(Color.WHITE);
        exportButton.setFocusPainted(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Student Statistics"));

        // Main statistics panel
        JPanel statsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add labels
        gbc.gridx = 0; gbc.gridy = 0;
        statsPanel.add(new JLabel("Overview:"), gbc);
        gbc.gridy = 1;
        statsPanel.add(totalStudentsLabel, gbc);
        gbc.gridy = 2;
        statsPanel.add(passedStudentsLabel, gbc);
        gbc.gridy = 3;
        statsPanel.add(failedStudentsLabel, gbc);

        gbc.gridy = 4;
        statsPanel.add(new JLabel(" "), gbc); // Spacer

        gbc.gridy = 5;
        statsPanel.add(new JLabel("Marks Analysis:"), gbc);
        gbc.gridy = 6;
        statsPanel.add(averageMarksLabel, gbc);
        gbc.gridy = 7;
        statsPanel.add(highestMarksLabel, gbc);
        gbc.gridy = 8;
        statsPanel.add(lowestMarksLabel, gbc);

        // Department statistics panel
        JPanel deptPanel = new JPanel(new BorderLayout());
        deptPanel.setBorder(BorderFactory.createTitledBorder("Department-wise Distribution"));
        deptPanel.add(new JScrollPane(departmentStatsArea), BorderLayout.CENTER);

        // Grade statistics panel
        JPanel gradePanel = new JPanel(new BorderLayout());
        gradePanel.setBorder(BorderFactory.createTitledBorder("Grade Distribution"));
        gradePanel.add(new JScrollPane(gradeStatsArea), BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton);

        // Layout arrangement
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(statsPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        rightPanel.add(deptPanel);
        rightPanel.add(gradePanel);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        refreshButton.addActionListener(e -> loadStatistics());
        exportButton.addActionListener(e -> exportStatistics());
    }

    public void loadStatistics() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            GradeCalculator.Statistics stats = GradeCalculator.calculateStatistics(students);

            // Update overview labels
            totalStudentsLabel.setText("Total Students: " + stats.getTotalStudents());
            passedStudentsLabel.setText(String.format("Passed: %d (%.1f%%)", 
                stats.getPassedStudents(), stats.getPassPercentage()));
            failedStudentsLabel.setText(String.format("Failed: %d (%.1f%%)", 
                stats.getFailedStudents(), stats.getFailPercentage()));

            // Update marks analysis
            averageMarksLabel.setText(String.format("Average Marks: %.2f", stats.getAverageMarks()));
            highestMarksLabel.setText(String.format("Highest Marks: %.2f", stats.getHighestMarks()));
            lowestMarksLabel.setText(String.format("Lowest Marks: %.2f", stats.getLowestMarks()));

            // Update department statistics
            updateDepartmentStats(stats.getDepartmentStats());

            // Update grade statistics
            updateGradeStats(stats.getGradeStats());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading statistics: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDepartmentStats(Map<String, Long> departmentStats) {
        StringBuilder sb = new StringBuilder();
        sb.append("Department\t\tCount\tPercentage\n");
        sb.append("==========================================\n");

        int totalStudents = departmentStats.values().stream().mapToInt(Long::intValue).sum();

        for (Map.Entry<String, Long> entry : departmentStats.entrySet()) {
            String dept = entry.getKey();
            long count = entry.getValue();
            double percentage = totalStudents > 0 ? (double) count / totalStudents * 100 : 0.0;
            
            sb.append(String.format("%-20s\t%d\t%.1f%%\n", dept, count, percentage));
        }

        departmentStatsArea.setText(sb.toString());
    }

    private void updateGradeStats(Map<String, Long> gradeStats) {
        StringBuilder sb = new StringBuilder();
        sb.append("Grade\tCount\tPercentage\n");
        sb.append("========================\n");

        int totalStudents = gradeStats.values().stream().mapToInt(Long::intValue).sum();

        // Define grade order
        String[] gradeOrder = {"A+", "A", "B", "C", "D", "F"};

        for (String grade : gradeOrder) {
            long count = gradeStats.getOrDefault(grade, 0L);
            double percentage = totalStudents > 0 ? (double) count / totalStudents * 100 : 0.0;
            
            sb.append(String.format("%s\t%d\t%.1f%%\n", grade, count, percentage));
        }

        gradeStatsArea.setText(sb.toString());
    }

    private void exportStatistics() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            GradeCalculator.Statistics stats = GradeCalculator.calculateStatistics(students);

            StringBuilder report = new StringBuilder();
            report.append("STUDENT STATISTICS REPORT\n");
            report.append("=========================\n\n");
            report.append("Generated on: ").append(java.time.LocalDateTime.now()).append("\n\n");
            
            report.append("OVERVIEW:\n");
            report.append("Total Students: ").append(stats.getTotalStudents()).append("\n");
            report.append("Passed: ").append(stats.getPassedStudents())
                  .append(" (").append(String.format("%.1f", stats.getPassPercentage())).append("%)\n");
            report.append("Failed: ").append(stats.getFailedStudents())
                  .append(" (").append(String.format("%.1f", stats.getFailPercentage())).append("%)\n\n");
            
            report.append("MARKS ANALYSIS:\n");
            report.append("Average: ").append(String.format("%.2f", stats.getAverageMarks())).append("\n");
            report.append("Highest: ").append(String.format("%.2f", stats.getHighestMarks())).append("\n");
            report.append("Lowest: ").append(String.format("%.2f", stats.getLowestMarks())).append("\n\n");
            
            report.append("DEPARTMENT DISTRIBUTION:\n");
            for (Map.Entry<String, Long> entry : stats.getDepartmentStats().entrySet()) {
                report.append(entry.getKey()).append(": ").append(entry.getValue()).append(" students\n");
            }
            
            report.append("\nGRADE DISTRIBUTION:\n");
            for (Map.Entry<String, Long> entry : stats.getGradeStats().entrySet()) {
                report.append(entry.getKey()).append(": ").append(entry.getValue()).append(" students\n");
            }

            // Save to file
            String filename = "statistics_report_" + 
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
                ".txt";
            
            try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filename))) {
                writer.print(report.toString());
            }
            
            JOptionPane.showMessageDialog(this, 
                "Statistics exported to: " + filename, 
                "Export Successful", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error exporting statistics: " + e.getMessage(), 
                "Export Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refresh() {
        loadStatistics();
    }
} 