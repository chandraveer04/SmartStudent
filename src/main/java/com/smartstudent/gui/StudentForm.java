package com.smartstudent.gui;

import com.smartstudent.dao.StudentDAO;
import com.smartstudent.model.Student;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StudentForm extends JDialog {
    private JTextField nameField;
    private JTextField rollNoField;
    private JComboBox<String> departmentCombo;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField marksField;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel gradeLabel;
    private JLabel statusLabel;
    
    private StudentDAO studentDAO;
    private Student student;
    private boolean confirmed = false;
    private MainFrame parentFrame;

    public StudentForm(MainFrame parent, Student student) {
        super(parent, "Student Form", true);
        this.parentFrame = parent;
        this.student = student;
        this.studentDAO = parent.getStudentDAO();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadDepartments();
        
        if (student != null) {
            setTitle("Edit Student");
            loadStudentData();
        } else {
            setTitle("Add New Student");
        }
        
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initializeComponents() {
        nameField = new JTextField(20);
        rollNoField = new JTextField(20);
        departmentCombo = new JComboBox<>();
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        marksField = new JTextField(20);
        
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        
        gradeLabel = new JLabel("Grade: -");
        statusLabel = new JLabel("Status: -");
        
        // Style buttons
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        
        // Add marks field listener for real-time grade calculation
        marksField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateGrade(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateGrade(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateGrade(); }
        });
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);
        
        // Roll No
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Roll No:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(rollNoField, gbc);
        
        // Department
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(departmentCombo, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(phoneField, gbc);
        
        // Marks
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Marks:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(marksField, gbc);
        
        // Grade and Status
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(gradeLabel);
        infoPanel.add(statusLabel);
        mainPanel.add(infoPanel, gbc);
        
        // Buttons
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> handleCancel());
        
        // Enter key for save
        getRootPane().setDefaultButton(saveButton);
    }

    private void loadDepartments() {
        try {
            List<String> departments = studentDAO.getAllDepartments();
            departmentCombo.addItem("Computer Science");
            departmentCombo.addItem("Electrical Engineering");
            departmentCombo.addItem("Mechanical Engineering");
            departmentCombo.addItem("Civil Engineering");
            departmentCombo.addItem("Information Technology");
            departmentCombo.addItem("Electronics & Communication");
            
            // Add any additional departments from database
            for (String dept : departments) {
                if (!isDepartmentInCombo(dept)) {
                    departmentCombo.addItem(dept);
                }
            }
        } catch (SQLException e) {
            showError("Error loading departments: " + e.getMessage());
        }
    }

    private boolean isDepartmentInCombo(String department) {
        for (int i = 0; i < departmentCombo.getItemCount(); i++) {
            if (departmentCombo.getItemAt(i).equals(department)) {
                return true;
            }
        }
        return false;
    }

    private void loadStudentData() {
        if (student != null) {
            nameField.setText(student.getName());
            rollNoField.setText(student.getRollNo());
            rollNoField.setEnabled(false); // Don't allow roll no change during edit
            departmentCombo.setSelectedItem(student.getDepartment());
            emailField.setText(student.getEmail());
            phoneField.setText(student.getPhone());
            marksField.setText(String.format("%.2f", student.getMarks()));
        }
    }

    private void updateGrade() {
        try {
            String marksText = marksField.getText().trim();
            if (!marksText.isEmpty()) {
                double marks = Double.parseDouble(marksText);
                if (marks >= 0 && marks <= 100) {
                    String grade = calculateGrade(marks);
                    boolean passed = marks >= 50;
                    
                    gradeLabel.setText("Grade: " + grade);
                    statusLabel.setText("Status: " + (passed ? "Pass" : "Fail"));
                    
                    // Color coding
                    if (passed) {
                        statusLabel.setForeground(new Color(0, 128, 0));
                    } else {
                        statusLabel.setForeground(new Color(220, 20, 60));
                    }
                } else {
                    gradeLabel.setText("Grade: Invalid");
                    statusLabel.setText("Status: Invalid");
                    statusLabel.setForeground(Color.BLACK);
                }
            } else {
                gradeLabel.setText("Grade: -");
                statusLabel.setText("Status: -");
                statusLabel.setForeground(Color.BLACK);
            }
        } catch (NumberFormatException e) {
            gradeLabel.setText("Grade: Invalid");
            statusLabel.setText("Status: Invalid");
            statusLabel.setForeground(Color.BLACK);
        }
    }

    private String calculateGrade(double marks) {
        if (marks >= 90) return "A+";
        else if (marks >= 80) return "A";
        else if (marks >= 70) return "B";
        else if (marks >= 60) return "C";
        else if (marks >= 50) return "D";
        else return "F";
    }

    private void handleSave() {
        if (!validateInput()) {
            return;
        }
        
        try {
            Student newStudent = new Student(
                nameField.getText().trim(),
                rollNoField.getText().trim(),
                (String) departmentCombo.getSelectedItem(),
                emailField.getText().trim(),
                phoneField.getText().trim(),
                Double.parseDouble(marksField.getText().trim())
            );
            
            if (student == null) {
                // Adding new student
                if (studentDAO.isRollNoExists(newStudent.getRollNo())) {
                    showError("A student with this Roll No already exists!");
                    return;
                }
                studentDAO.insertStudent(newStudent);
                JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Updating existing student
                studentDAO.updateStudent(student.getRollNo(), newStudent);
                JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            
            confirmed = true;
            dispose();
            
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            showError("Invalid marks format");
        }
    }

    private boolean validateInput() {
        // Validate name
        if (nameField.getText().trim().isEmpty()) {
            showError("Please enter student name");
            nameField.requestFocus();
            return false;
        }
        
        // Validate roll no
        if (rollNoField.getText().trim().isEmpty()) {
            showError("Please enter roll number");
            rollNoField.requestFocus();
            return false;
        }
        
        // Validate department
        if (departmentCombo.getSelectedItem() == null) {
            showError("Please select a department");
            departmentCombo.requestFocus();
            return false;
        }
        
        // Validate email
        String email = emailField.getText().trim();
        if (!email.isEmpty() && !isValidEmail(email)) {
            showError("Please enter a valid email address");
            emailField.requestFocus();
            return false;
        }
        
        // Validate phone
        String phone = phoneField.getText().trim();
        if (!phone.isEmpty() && !isValidPhone(phone)) {
            showError("Please enter a valid phone number");
            phoneField.requestFocus();
            return false;
        }
        
        // Validate marks
        try {
            double marks = Double.parseDouble(marksField.getText().trim());
            if (marks < 0 || marks > 100) {
                showError("Marks must be between 0 and 100");
                marksField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter valid marks");
            marksField.requestFocus();
            return false;
        }
        
        return true;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("^[0-9]{10,15}$");
    }

    private void handleCancel() {
        confirmed = false;
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
} 