@echo off
REM ==============================================================================
REM  Campus Lost & Found Management System - Automated Test Runner
REM ==============================================================================

if not exist "bin" mkdir bin
if not exist "reports" mkdir reports

echo Compiling Application & Academic Validation Test Suite...
javac -d bin src\com\campus\lostfound\model\*.java ^
             src\com\campus\lostfound\exception\*.java ^
             src\com\campus\lostfound\util\*.java ^
             src\com\campus\lostfound\dao\*.java ^
             src\com\campus\lostfound\thread\*.java ^
             src\com\campus\lostfound\service\*.java ^
             src\com\campus\lostfound\Main.java ^
             test\com\campus\lostfound\LostFoundValidationTest.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Test compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo Executing Automated Validation Test Suite...
java -cp bin com.campus.lostfound.LostFoundValidationTest
pause
