@echo off

:: Start Java backend (Maven Spring Boot) in parallel
echo Starting Java backend...
start /d "%~dp0" cmd /c "mvn spring-boot:run"

:: Start Python backend (both app.py and matching_service.py) in parallel
echo Starting Python backend...
start /d "%~dp0python_service" cmd /c "python app.py"
start /d "%~dp0python_service" cmd /c "python matching_service.py"

:: Start Vue.js frontend in parallel
echo Starting Vue.js server...
start /d "%~dp0src\frontend" cmd /c "npm run serve"

echo All servers started.
pause
