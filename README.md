# Smart Student Management System

A comprehensive Java-based student management system with GUI interface and MySQL database integration.

## 🚀 Features

### 🔐 Admin Login System
- Secure login authentication
- MySQL-based user management
- Session management

### 📋 Student Data Management (CRUD Operations)
- **Create**: Add new students with complete information
- **Read**: View all students in organized table format
- **Update**: Modify existing student records
- **Delete**: Remove student records
- **Search**: Find students by name, roll number, or department

### 🔍 Advanced Search & Filter
- Search by Roll Number
- Search by Department
- Filter by Marks range (e.g., students with >80 marks)
- Multiple search criteria support

### 📊 Statistics Module
- Total student count
- Highest and lowest marks analysis
- Department-wise student distribution
- Performance analytics

### 💾 Database Integration
- MySQL database with JDBC connectivity
- Optimized queries for performance
- Data persistence and backup

### 🧱 Additional Features
- Modern Java Swing GUI
- Export functionality (CSV/Text)
- Subject-wise marks tracking
- Grade calculation system
- Responsive design

## 🛠️ Technology Stack

- **Language**: Java 8+
- **GUI Framework**: Java Swing
- **Database**: MySQL
- **Database Connectivity**: JDBC
- **Build Tool**: Maven (optional)

## 📁 Project Structure

```
SmartStudent/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── smartstudent/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── Student.java
│   │   │   │   │   │   └── User.java
│   │   │   │   │   ├── dao/
│   │   │   │   │   │   ├── StudentDAO.java
│   │   │   │   │   │   └── UserDAO.java
│   │   │   │   │   ├── gui/
│   │   │   │   │   │   ├── LoginFrame.java
│   │   │   │   │   │   ├── MainFrame.java
│   │   │   │   │   │   ├── StudentForm.java
│   │   │   │   │   │   └── StatisticsPanel.java
│   │   │   │   │   ├── util/
│   │   │   │   │   │   ├── DatabaseConnection.java
│   │   │   │   │   │   ├── ExportUtil.java
│   │   │   │   │   │   └── GradeCalculator.java
│   │   │   │   │   └── SmartStudentApp.java
│   │   │   └── resources/
│   │   │       └── database.sql
├── lib/
│   └── mysql-connector-java.jar
├── config/
│   └── database.properties
└── README.md
```

## 🚀 Quick Start

1. **Database Setup**:
   ```sql
   -- Run the database.sql script to create tables
   ```

2. **Configuration**:
   - Update `config/database.properties` with your MySQL credentials

3. **Compile and Run**:
   ```bash
   IN ROOT FOLDER : ./compile.bat
   IN ROOT FOLDER : ./run.bat
   ```

## 📋 Database Schema

### Students Table
```sql
CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    roll_no VARCHAR(20) UNIQUE NOT NULL,
    department VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(15),
    marks DECIMAL(5,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Users Table
```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'admin',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🔧 Configuration

Update `config/database.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/smartstudent
db.username=your_username
db.password=your_password
db.driver=com.mysql.cj.jdbc.Driver
```

## 📊 Features Overview

| Feature | Status | Description |
|---------|--------|-------------|
| Admin Login | ✅ | Secure authentication system |
| CRUD Operations | ✅ | Complete student data management |
| Search & Filter | ✅ | Advanced search capabilities |
| Statistics | ✅ | Comprehensive analytics |
| Database Integration | ✅ | MySQL with JDBC |
| GUI Interface | ✅ | Modern Swing interface |
| Export Functionality | ✅ | CSV/Text export |
| Grade Calculation | ✅ | Subject-wise grading |

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

Smart Student Management System - Developed by Chandraveer Singh Rajput for educational purposes only please do not copy 
