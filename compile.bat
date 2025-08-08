@echo off
echo ========================================
echo Smart Student Management System - Compilation
echo ========================================
echo.

echo Creating output directory...
if not exist "build" mkdir build

echo Compiling Java files...
javac -cp "lib/*" -d build ^
  src/main/java/com/smartstudent/util/DatabaseConnection.java ^
  src/main/java/com/smartstudent/util/ExportUtil.java ^
  src/main/java/com/smartstudent/util/GradeCalculator.java ^
  src/main/java/com/smartstudent/model/Student.java ^
  src/main/java/com/smartstudent/model/User.java ^
  src/main/java/com/smartstudent/dao/StudentDAO.java ^
  src/main/java/com/smartstudent/dao/UserDAO.java ^
  src/main/java/com/smartstudent/gui/LoginFrame.java ^
  src/main/java/com/smartstudent/gui/MainFrame.java ^
  src/main/java/com/smartstudent/gui/StudentForm.java ^
  src/main/java/com/smartstudent/SmartStudentApp.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo.
echo To run the application, use: run.bat
echo.

pause 