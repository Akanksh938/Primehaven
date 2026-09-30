@echo off
setlocal
set "PROJECT_ROOT=%~dp0"
set "BACKEND_ROOT=%PROJECT_ROOT%backend"
set "FRONTEND_ROOT=%PROJECT_ROOT%frontend"
set "MAVEN_BIN=C:\Users\User\.maven\maven-3.9.15\bin"

if not defined PRIMEHAVEN_DB_USER set "PRIMEHAVEN_DB_USER=root"
if not defined PRIMEHAVEN_DB_PASSWORD (
  set /p "PRIMEHAVEN_DB_PASSWORD=Enter MySQL password: "
)

call "%PROJECT_ROOT%stop-primehaven.cmd"

echo Building backend services...
pushd "%BACKEND_ROOT%"
if exist "%MAVEN_BIN%\mvn.cmd" (
  call "%MAVEN_BIN%\mvn.cmd" clean package -DskipTests
) else (
  call mvn clean package -DskipTests
)
if errorlevel 1 (
  popd
  echo Backend build failed.
  exit /b 1
)
popd

call :startService "Eureka" "%BACKEND_ROOT%\eureka-server\target\eureka-server-0.0.1-SNAPSHOT.jar" "%BACKEND_ROOT%\eureka.log" "%BACKEND_ROOT%\eureka.err"
call :waitFor "http://localhost:8761"
call :startService "Property" "%BACKEND_ROOT%\property-service\target\property-service-0.0.1-SNAPSHOT.jar" "%BACKEND_ROOT%\property.log" "%BACKEND_ROOT%\property.err"
call :waitFor "http://localhost:8081/api/properties"
call :startService "Visit" "%BACKEND_ROOT%\visit-service\target\visit-service-0.0.1-SNAPSHOT.jar" "%BACKEND_ROOT%\visit.log" "%BACKEND_ROOT%\visit.err"
call :waitFor "http://localhost:8082/api/visits"
call :startService "Inquiry" "%BACKEND_ROOT%\inquiry-service\target\inquiry-service-0.0.1-SNAPSHOT.jar" "%BACKEND_ROOT%\inquiry.log" "%BACKEND_ROOT%\inquiry.err"
call :waitFor "http://localhost:8083/api/inquiries"
call :startService "Gateway" "%BACKEND_ROOT%\api-gateway\target\api-gateway-0.0.1-SNAPSHOT.jar" "%BACKEND_ROOT%\gateway.log" "%BACKEND_ROOT%\gateway.err"
call :waitFor "http://localhost:8080/api/properties"

echo Starting frontend...
start "PrimeHaven Frontend" /b /D "%FRONTEND_ROOT%" npm.cmd run dev -- --host 0.0.0.0 > "%FRONTEND_ROOT%\frontend.log" 2> "%FRONTEND_ROOT%\frontend.err"
call :waitFor "http://localhost:5173"

echo.
echo PrimeHaven is running.
echo Frontend: http://localhost:5173
echo Gateway:  http://localhost:8080/api/properties
exit /b 0

:startService
echo Starting %~1...
start "%~1" /b java -jar "%~2" > "%~3" 2> "%~4"
exit /b 0

:waitFor
for /L %%I in (1,1,45) do (
  curl.exe -fsS "%~1" >nul 2>nul && exit /b 0
  timeout /t 2 >nul
)
echo Timed out waiting for %~1
exit /b 1
