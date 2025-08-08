@echo off
echo ========================================
echo Smart Student Management System
echo ========================================
echo.

if not exist "build" (
    echo Build directory not found. Please run compile.bat first.
    pause
    exit /b 1
)

echo Running Smart Student Management System...
echo.
echo Default login credentials:
echo Username: admin
echo Password: admin123
echo.

java -cp "lib/*;build" com.smartstudent.SmartStudentApp

pause 