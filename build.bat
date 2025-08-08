@echo off
echo ========================================
echo Smart Student Management System
echo ========================================
echo.

echo Compiling Java files...
javac -cp "lib/*" -d . src/main/java/com/smartstudent/*.java src/main/java/com/smartstudent/*/*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo.
echo Running application...
echo.

java -cp "lib/*;." com.smartstudent.SmartStudentApp

pause 