@echo off
REM ==============================================================================
REM  Campus Lost & Found Management System - Build & Run Script
REM  Vellore Institute of Technology (VIT) - VITyarthi Evaluation
REM ==============================================================================

echo [1/3] Creating output directories...
if not exist "bin" mkdir bin
if not exist "reports" mkdir reports

echo [2/3] Compiling Java Source Files (Units 1-5)...
javac -d bin src\com\campus\lostfound\model\*.java ^
             src\com\campus\lostfound\exception\*.java ^
             src\com\campus\lostfound\util\*.java ^
             src\com\campus\lostfound\dao\*.java ^
             src\com\campus\lostfound\thread\*.java ^
             src\com\campus\lostfound\service\*.java ^
             src\com\campus\lostfound\Main.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed! Check JDK installation.
    pause
    exit /b %ERRORLEVEL%
)

echo [3/3] Launching Campus Lost & Found Management System...
echo ==============================================================================
java -cp bin com.campus.lostfound.Main %*
pause
