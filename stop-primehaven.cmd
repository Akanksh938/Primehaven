@echo off
setlocal
for %%P in (8761 8080 8081 8082 8083 5173) do (
  for /f "tokens=5" %%A in ('netstat -ano ^| findstr /R /C:":%%P .*LISTENING"') do (
    taskkill /F /PID %%A >nul 2>nul
  )
)
echo PrimeHaven processes stopped.
