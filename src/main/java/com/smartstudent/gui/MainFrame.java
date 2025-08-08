package com.smartstudent.gui;

import com.smartstudent.dao.StudentDAO;
import com.smartstudent.model.Student;
import com.smartstudent.util.ExportUtil;
import com.smartstudent.util.GradeCalculator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class MainFrame extends JFrame {
    private StudentDAO studentDAO;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JLabel statusLabel;
    
    // Menu components
    private JMenuBar menuBar;
    private JMenu fileMenu, studentMenu, searchMenu, statisticsMenu, helpMenu;

    public MainFrame() {
        studentDAO = new StudentDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadStudents();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Smart Student Management System");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initializeComponents() {
        // Initialize table
        String[] columnNames = {"ID", "Name", "Roll No", "Department", "Email", "Phone", "Marks", "Grade", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getTableHeader().setReorderingAllowed(false);
        
        // Initialize search components
        searchField = new JTextField(20);
        String[] searchTypes = {"All", "Name", "Roll No", "Department", "Marks Range"};
        searchTypeCombo = new JComboBox<>(searchTypes);
        
        // Initialize status label
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Initialize menu bar
        setupMenuBar();
    }

    private void setupMenuBar() {
        menuBar = new JMenuBar();
        
        // File Menu
        fileMenu = new JMenu("File");
        JMenuItem exportCsvItem = new JMenuItem("Export to CSV");
        JMenuItem exportTextItem = new JMenuItem("Export to Text");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        fileMenu.add(exportCsvItem);
        fileMenu.add(exportTextItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Student Menu
        studentMenu = new JMenu("Student");
        JMenuItem addStudentItem = new JMenuItem("Add Student");
        JMenuItem editStudentItem = new JMenuItem("Edit Student");
        JMenuItem deleteStudentItem = new JMenuItem("Delete Student");
        JMenuItem refreshItem = new JMenuItem("Refresh");
        
        studentMenu.add(addStudentItem);
        studentMenu.add(editStudentItem);
        studentMenu.add(deleteStudentItem);
        studentMenu.addSeparator();
        studentMenu.add(refreshItem);
        
        // Search Menu
        searchMenu = new JMenu("Search");
        JMenuItem searchByNameItem = new JMenuItem("Search by Name");
        JMenuItem searchByRollItem = new JMenuItem("Search by Roll No");
        JMenuItem searchByDeptItem = new JMenuItem("Search by Department");
        JMenuItem searchByMarksItem = new JMenuItem("Search by Marks Range");
        
        searchMenu.add(searchByNameItem);
        searchMenu.add(searchByRollItem);
        searchMenu.add(searchByDeptItem);
        searchMenu.add(searchByMarksItem);
        
        // Statistics Menu
        statisticsMenu = new JMenu("Statistics");
        JMenuItem viewStatsItem = new JMenuItem("View Statistics");
        JMenuItem topPerformersItem = new JMenuItem("Top Performers");
        
        statisticsMenu.add(viewStatsItem);
        statisticsMenu.add(topPerformersItem);
        
        // Help Menu
        helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);
        
        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(studentMenu);
        menuBar.add(searchMenu);
        menuBar.add(statisticsMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
        
        // Add event handlers
        exportCsvItem.addActionListener(e -> exportToCSV());
        exportTextItem.addActionListener(e -> exportToText());
        exitItem.addActionListener(e -> System.exit(0));
        
        addStudentItem.addActionListener(e -> showAddStudentDialog());
        editStudentItem.addActionListener(e -> editSelectedStudent());
        deleteStudentItem.addActionListener(e -> deleteSelectedStudent());
        refreshItem.addActionListener(e -> loadStudents());
        
        searchByNameItem.addActionListener(e -> searchByName());
        searchByRollItem.addActionListener(e -> searchByRollNo());
        searchByDeptItem.addActionListener(e -> searchByDepartment());
        searchByMarksItem.addActionListener(e -> searchByMarksRange());
        
        viewStatsItem.addActionListener(e -> showStatistics());
        topPerformersItem.addActionListener(e -> showTopPerformers());
        
        aboutItem.addActionListener(e -> showAboutDialog());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel for search
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(new JLabel("Type:"));
        topPanel.add(searchTypeCombo);
        
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        topPanel.add(searchButton);
        
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            loadStudents();
        });
        topPanel.add(clearButton);
        
        // Center panel with table
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Student Records"));
        
        // Bottom panel for status
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusLabel, BorderLayout.WEST);
        
        // Add components
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        // Double-click to edit student
        studentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editSelectedStudent();
                }
            }
        });
    }

    private void loadStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            updateTable(students);
            updateStatus("Loaded " + students.size() + " students");
        } catch (SQLException e) {
            showError("Error loading students: " + e.getMessage());
        }
    }

    private void updateTable(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            Vector<Object> row = new Vector<>();
            row.add(student.getId());
            row.add(student.getName());
            row.add(student.getRollNo());
            row.add(student.getDepartment());
            row.add(student.getEmail());
            row.add(student.getPhone());
            row.add(String.format("%.2f", student.getMarks()));
            row.add(student.getGrade());
            row.add(student.isPassed() ? "Pass" : "Fail");
            tableModel.addRow(row);
        }
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        String searchType = (String) searchTypeCombo.getSelectedItem();
        
        if (searchText.isEmpty()) {
            loadStudents();
            return;
        }
        
        try {
            List<Student> results = null;
            switch (searchType) {
                case "Name":
                    results = studentDAO.searchByName(searchText);
                    break;
                case "Roll No":
                    results = studentDAO.searchByRollNo(searchText);
                    break;
                case "Department":
                    results = studentDAO.searchByDepartment(searchText);
                    break;
                case "Marks Range":
                    try {
                        String[] range = searchText.split("-");
                        if (range.length == 2) {
                            double min = Double.parseDouble(range[0].trim());
                            double max = Double.parseDouble(range[1].trim());
                            results = studentDAO.searchByMarksRange(min, max);
                        } else {
                            showError("Please enter marks range as 'min-max' (e.g., 70-90)");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        showError("Invalid marks range format");
                        return;
                    }
                    break;
                default:
                    loadStudents();
                    return;
            }
            
            updateTable(results);
            updateStatus("Found " + results.size() + " students");
        } catch (SQLException e) {
            showError("Error searching: " + e.getMessage());
        }
    }

    private void showAddStudentDialog() {
        StudentForm dialog = new StudentForm(this, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            loadStudents();
        }
    }

    private void editSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a student to edit");
            return;
        }
        
        String rollNo = (String) tableModel.getValueAt(selectedRow, 2);
        try {
            Student student = studentDAO.getStudentByRollNo(rollNo);
            if (student != null) {
                StudentForm dialog = new StudentForm(this, student);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    loadStudents();
                }
            }
        } catch (SQLException e) {
            showError("Error loading student: " + e.getMessage());
        }
    }

    private void deleteSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a student to delete");
            return;
        }
        
        String rollNo = (String) tableModel.getValueAt(selectedRow, 2);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete student '" + name + "' (Roll No: " + rollNo + ")?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                studentDAO.deleteStudent(rollNo);
                loadStudents();
                updateStatus("Student deleted successfully");
            } catch (SQLException e) {
                showError("Error deleting student: " + e.getMessage());
            }
        }
    }

    private void exportToCSV() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            ExportUtil.exportToCSV(students, null);
            updateStatus("Data exported to CSV successfully");
        } catch (Exception e) {
            showError("Error exporting to CSV: " + e.getMessage());
        }
    }

    private void exportToText() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            ExportUtil.exportToText(students, null);
            updateStatus("Data exported to text successfully");
        } catch (Exception e) {
            showError("Error exporting to text: " + e.getMessage());
        }
    }

    private void searchByName() {
        searchTypeCombo.setSelectedItem("Name");
        searchField.requestFocus();
    }

    private void searchByRollNo() {
        searchTypeCombo.setSelectedItem("Roll No");
        searchField.requestFocus();
    }

    private void searchByDepartment() {
        searchTypeCombo.setSelectedItem("Department");
        searchField.requestFocus();
    }

    private void searchByMarksRange() {
        searchTypeCombo.setSelectedItem("Marks Range");
        searchField.setText("70-90");
        searchField.requestFocus();
        searchField.selectAll();
    }

    private void showStatistics() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            GradeCalculator.Statistics stats = GradeCalculator.calculateStatistics(students);
            
            StringBuilder message = new StringBuilder();
            message.append("STUDENT STATISTICS\n");
            message.append("==================\n\n");
            message.append("Total Students: ").append(stats.getTotalStudents()).append("\n");
            message.append("Passed: ").append(stats.getPassedStudents()).append(" (").append(String.format("%.1f", stats.getPassPercentage())).append("%)\n");
            message.append("Failed: ").append(stats.getFailedStudents()).append(" (").append(String.format("%.1f", stats.getFailPercentage())).append("%)\n\n");
            message.append("Marks Analysis:\n");
            message.append("Average: ").append(String.format("%.2f", stats.getAverageMarks())).append("\n");
            message.append("Highest: ").append(String.format("%.2f", stats.getHighestMarks())).append("\n");
            message.append("Lowest: ").append(String.format("%.2f", stats.getLowestMarks())).append("\n\n");
            
            message.append("Department-wise Distribution:\n");
            for (var entry : stats.getDepartmentStats().entrySet()) {
                message.append(entry.getKey()).append(": ").append(entry.getValue()).append(" students\n");
            }
            
            JOptionPane.showMessageDialog(this, message.toString(), "Statistics", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            showError("Error loading statistics: " + e.getMessage());
        }
    }

    private void showTopPerformers() {
        try {
            List<Student> topStudents = studentDAO.getTopPerformers(10);
            if (topStudents.isEmpty()) {
                showError("No students found");
                return;
            }
            
            StringBuilder message = new StringBuilder();
            message.append("TOP 10 PERFORMERS\n");
            message.append("=================\n\n");
            
            for (int i = 0; i < topStudents.size(); i++) {
                Student student = topStudents.get(i);
                message.append(String.format("%d. %s (%s) - %.2f marks (%s)\n", 
                    i + 1, student.getName(), student.getRollNo(), 
                    student.getMarks(), student.getGrade()));
            }
            
            JOptionPane.showMessageDialog(this, message.toString(), "Top Performers", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            showError("Error loading top performers: " + e.getMessage());
        }
    }

    private void showAboutDialog() {
        String aboutMessage = "Smart Student Management System\n" +
            "Version 1.0\n\n" +
            "A comprehensive Java-based student management system\n" +
            "with GUI interface and MySQL database integration.\n\n" +
            "Features:\n" +
            "• Complete CRUD operations\n" +
            "• Advanced search and filtering\n" +
            "• Statistics and analytics\n" +
            "• Export functionality\n" +
            "• Grade calculation\n\n" +
            "Developed by Chandraveer Singh Rajput for educational purposes only please do not copy ";
        
        JOptionPane.showMessageDialog(this, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public StudentDAO getStudentDAO() {
        return studentDAO;
    }
} 