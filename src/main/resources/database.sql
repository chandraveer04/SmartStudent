-- Create database
CREATE DATABASE IF NOT EXISTS smartstudent;
USE smartstudent;

-- Create users table for authentication
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'admin',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create students table with enhanced fields
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    roll_no VARCHAR(50) NOT NULL UNIQUE,
    department VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    marks DECIMAL(5,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default admin user
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin');

-- Insert sample students for testing
INSERT INTO students (name, roll_no, department, email, phone, marks) VALUES
('Jane Smith', 'CS002', 'Computer Science', 'jane.smith@email.com', '1234567891', 92.0),
('Mike Johnson', 'EE001', 'Electrical Engineering', 'mike.johnson@email.com', '1234567892', 78.5),
('Sarah Wilson', 'ME001', 'Mechanical Engineering', 'sarah.wilson@email.com', '1234567893', 88.0),
('David Brown', 'CS003', 'Computer Science', 'david.brown@email.com', '1234567894', 76.5); 