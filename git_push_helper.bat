@echo off
REM ==============================================================================
REM  GitHub Push Helper Script - Campus Lost & Found Management System
REM ==============================================================================

echo [1/4] Initializing Git repository...
git init

echo [2/4] Adding all project files (ignoring bin and temporary logs)...
git add .

echo [3/4] Creating initial commit...
git commit -m "Initial commit: Campus Lost & Found Management System (VITyarthi Project Units 1-5)"

echo [4/4] Next Steps:
echo.
echo Please run the following two commands to link and push to your GitHub:
echo ----------------------------------------------------------------------
echo git branch -M main
echo git remote add origin https://github.com/YOUR_USERNAME/Campus-Lost-and-Found-System.git
echo git push -u origin main
echo ----------------------------------------------------------------------
echo.
pause
