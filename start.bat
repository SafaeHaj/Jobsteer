@echo off

:: Start Java backend (Maven Spring Boot) in parallel
echo Starting Java backend...
start /d "%~dp0backend" cmd /c "mvn spring-boot:run"

:: Start Python backend (both app.py and matching_service.py) in parallel
echo Starting Python backend...
start /d "%~dp0backend\python_service" cmd /c "python app.py"
start /d "%~dp0backend\python_service" cmd /c "python matching_service.py"
start /d "%~dp0backend\python_service" cmd /c "python app1.py"

:: Check and start Vue.js frontend in parallel
echo Checking if vue-cli-service is installed...
cd "%~dp0frontend"

IF EXIST "node_modules\@vue\cli-service\bin\vue-cli-service.js" (
    echo vue-cli-service is already installed.
) ELSE (
    echo vue-cli-service is not installed. Installing now...
    npm install @vue/cli-service --save-dev
    IF ERRORLEVEL 1 (
        echo Failed to install vue-cli-service. Exiting...
        exit /b 1
    )
)

echo Starting Vue.js server...
start cmd /c "npm run serve"

:: Final message
echo All servers started.
pause